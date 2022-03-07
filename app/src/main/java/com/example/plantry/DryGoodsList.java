package com.example.plantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class DryGoodsList extends AppCompatActivity {
    RecyclerView recyclerView;
    ItemsAdapter items;
    TextView categoryHeading, emptyListLbl;
    EditText name;
    DatePicker expiryDate;
    Boolean isStaples;
    ImageButton addItem, searchItem, editItem;
    AppCompatButton addItemButton, cancelButton;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference householdDB = database.getReference().child("household");
    private DatabaseReference userDB = database.getReference().child("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        categoryHeading = findViewById(R.id.category_name);
        categoryHeading.setText("Dry Goods");
        emptyListLbl = findViewById(R.id.emptyList_lbl);
        addItem = findViewById(R.id.add_item);
        addItem.setOnClickListener(View ->{
            addItem();
            items.notifyDataSetChanged();
        });

        // get items info by looping through the correct DB
        HashSet<Item> itemsList = new HashSet<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            for (UserInfo profile : user.getProviderData()){
                // loop through database to find user's profile, obtain household owner email
                userDB.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("email").getValue().equals(profile.getEmail())){
                                String owner = dataSnapshot.child("householdOwner").getValue().toString();

                                // loop through database to find household
                                householdDB.orderByChild("ownerEmail").equalTo(owner).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            for(DataSnapshot data : snapshot.getChildren()){
                                                String key = data.getKey();
                                                householdDB.child(key).child("dryGoods").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                        for (DataSnapshot data1 : snapshot1.getChildren()){
                                                            String name = data1.child("itemName").getValue().toString();
                                                            String addedBy = data1.child("addedBy").getValue().toString();
                                                            long expiryDate = (long) data1.child("expiryDate").getValue();
                                                            Boolean onSale = (Boolean) data1.child("onSale").getValue();
                                                            Boolean staples = (Boolean) data1.child("staples").getValue();

                                                            Item singleItem = new Item(name, expiryDate, addedBy, staples, onSale);

                                                            itemsList.add(singleItem);
                                                            items.notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(DryGoodsList.this, "Add item failed. Please try again", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        if(itemsList.isEmpty()){
            emptyListLbl.setText("List is empty");
        }

        // display items in recycler view
        recyclerView=findViewById(R.id.items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = new ItemsAdapter(this,itemsList);
        recyclerView.setAdapter(items);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this.getApplicationContext(), Overview.class);
        startActivity(intent);
    }

    private void addItem(){
        dialogBuilder = new AlertDialog.Builder((this));
        final View addItemPopupView = getLayoutInflater().inflate(R.layout.popup_additem, null);

        addItemButton = addItemPopupView.findViewById(R.id.add_item);
        cancelButton = addItemPopupView.findViewById(R.id.cancel_addItem);

        dialogBuilder.setView(addItemPopupView);
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        // perform add items to DB
        addItemButton.setOnClickListener(new View.OnClickListener(){

            // flag to solve double entry problem
            Boolean isAdded = Boolean.FALSE;

            @Override
            public void onClick(View v){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    for (UserInfo profile : user.getProviderData()){
                        String displayName = profile.getDisplayName();
                        name = (EditText) addItemPopupView.findViewById(R.id.name_addItem);
                        String productName = name.getEditableText().toString();
                        expiryDate = (DatePicker) addItemPopupView.findViewById(R.id.picker_expiry_date);
                        int day = expiryDate.getDayOfMonth() + 1;
                        int month = expiryDate.getMonth();
                        int year = expiryDate.getYear();

                        Calendar calendar = new GregorianCalendar(year, month, day);
                        long expiration = calendar.getTimeInMillis();

                        // loop through database to find user's profile, obtain household owner email
                        userDB.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    if(dataSnapshot.child("email").getValue().equals(profile.getEmail())){
                                        String owner = dataSnapshot.child("householdOwner").getValue().toString();

                                        // loop through database to find household
                                        householdDB.orderByChild("ownerEmail").equalTo(owner).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    for(DataSnapshot data : snapshot.getChildren()){
                                                        String key = data.getKey();
                                                        Item dryGoodsItem = new Item(productName, expiration, displayName, isStaples, false);

                                                        if(!isAdded){
                                                            householdDB.child(key).child("dryGoods").push().setValue(dryGoodsItem);
                                                            //    householdDB.child(key).child("dryGoods").child(productName).setValue(dryGoodsItem);
                                                            Toast.makeText(DryGoodsList.this, productName + " added successfully", Toast.LENGTH_SHORT).show();
                                                            isAdded = Boolean.TRUE;
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(DryGoodsList.this, "Add item failed. Please try again", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                                isAdded = Boolean.FALSE;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), DryGoodsList.class);
                startActivity(intent);
            }
        });
    }

    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.checkbox_staples:
                if(checked)
                    isStaples = Boolean.TRUE;
                else
                    isStaples = Boolean.FALSE;
        }
    }

    // TODO: edit item functionality
    // TODO: search item functionality
}
