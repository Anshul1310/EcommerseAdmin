package com.anstudios.ecommerseadmin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String[] spinnerUnitArr = new String[]{"Dozen", "Unit", "Kilogram", "Litres"};
    private EditText title, description, price;
    private ImageView smallImage, bigImage;
    private TextView unit;
    private String categoryItem;
    private String productId;
    private boolean isSmallImageChanged, isBigImageChanged, isold;
    private String smallurl, bigurl;
    private ProgressDialog progressDialog;
    private Uri smallUri, bigUri;
    private CardView saveBtn, deleteBtn;
    private Spinner unitSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        categoryItem = getIntent().getStringExtra("category");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Your catalogue is being added.");
        progressDialog.setCanceledOnTouchOutside(false);
        title = findViewById(R.id.add_product_title);
        description = findViewById(R.id.add_product_description);
        saveBtn = findViewById(R.id.add_product_saveBtn);
        price = findViewById(R.id.add_product_price);
        categoryItem = getIntent().getStringExtra("category");
        unitSpinner = findViewById(R.id.add_product_spinner);
        smallImage = findViewById(R.id.add_product_smallImage);
        bigImage = findViewById(R.id.add_product_bigImage);
        unit = findViewById(R.id.add_product_unit_measuring);
        deleteBtn = findViewById(R.id.add_product_deleteBtn);
        if (getIntent().getStringExtra("productId") == null) {
            deleteBtn.setVisibility(View.GONE);
            productId = UUID.randomUUID().toString();
            isold = false;
        } else {
            isold = true;
            productId = getIntent().getStringExtra("productId");
            title.setText(getIntent().getStringExtra("title"));
            description.setText(getIntent().getStringExtra("description"));
            Picasso.get().load(getIntent().getStringExtra("bigImage")).into(bigImage);
            bigurl = getIntent().getStringExtra("bigImage");
            isold = true;
            categoryItem = getIntent().getStringExtra("category");
            smallurl = getIntent().getStringExtra("smallImage");
            Picasso.get().load(getIntent().getStringExtra("smallImage")).into(smallImage);
            Toast.makeText(this, getIntent().getStringExtra("measuringUnit"), Toast.LENGTH_SHORT).show();
            price.setText(getIntent().getStringExtra("price"));
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(AddProduct.this)
                            .setTitle("Are you sure")
                            .setMessage("This item will get deleted forever from the database.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance().getReference("products")
                                            .child(productId).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(AddProduct.this, MainActivity.class));
                                            Toast.makeText(AddProduct.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
        categoryItem = getIntent().getStringExtra("category");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Your catalogue is being added.");
        progressDialog.setCanceledOnTouchOutside(false);
        title = findViewById(R.id.add_product_title);
        description = findViewById(R.id.add_product_description);
        saveBtn = findViewById(R.id.add_product_saveBtn);
        price = findViewById(R.id.add_product_price);
        categoryItem = getIntent().getStringExtra("category");
        unitSpinner = findViewById(R.id.add_product_spinner);
        smallImage = findViewById(R.id.add_product_smallImage);
        bigImage = findViewById(R.id.add_product_bigImage);
        unit = findViewById(R.id.add_product_unit_measuring);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, spinnerUnitArr
        );
        unitSpinner.setAdapter(ad);
        unitSpinner.setOnItemSelectedListener(this);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isErrorsInCatalogue()) {
                    uploadImageToDb();
                }
            }
        });
    }


    public void addImageSmallBtn(View view) {
        Dexter.withContext(AddProduct.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(i, "Select Your Profile Image"), 131);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

    }

    public void addImageBigBtn(View view) {
        Dexter.withContext(AddProduct.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(i, "Select Your Profile Image"), 132);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 131 && resultCode == RESULT_OK && data != null) {
            smallUri = data.getData();
            try {
                Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), smallUri);
                smallImage.setImageBitmap(b);
                isSmallImageChanged = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bigUri = data.getData();
            try {
                isBigImageChanged = true;
                Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), bigUri);
                bigImage.setImageBitmap(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isErrorsInCatalogue() {
        boolean temp = false;
        if (title.getText().toString().isEmpty()) {
            temp = true;
            Toast.makeText(this, "Invalid Title", Toast.LENGTH_SHORT).show();
        } else if (description.getText().toString().isEmpty()) {
            temp = true;
            Toast.makeText(this, "Invalid Description", Toast.LENGTH_SHORT).show();
        } else if (smallUri == null && !isold) {
            Toast.makeText(this, "No Small Image found", Toast.LENGTH_SHORT).show();
            temp = true;
        } else if (bigUri == null && !isold) {
            Toast.makeText(this, "No Big Image found", Toast.LENGTH_SHORT).show();
            temp = true;
        } else if (price.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please define a price", Toast.LENGTH_SHORT).show();
            temp = true;
        }
        return temp;
    }


    private void saveProductToDb() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("title", title.getText().toString());
        hashMap.put("description", description.getText().toString());
        hashMap.put("smallImage", smallurl);
        hashMap.put("bigImage", bigurl);
        hashMap.put("category", categoryItem);
        hashMap.put("measuringUnit", unit.getText().toString());
        hashMap.put("price", price.getText().toString());
        FirebaseDatabase.getInstance().getReference("products").child(productId)
                .setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.cancel();
                startActivity(new Intent(AddProduct.this, MainActivity.class));
                Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImageToDb() {
        try {
            progressDialog.show();
            if (isold) {
                if (isBigImageChanged && !isSmallImageChanged) {
                    FirebaseStorage.getInstance().getReference("products").child(productId)
                            .child("bigImage").putFile(bigUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseStorage.getInstance().getReference("products").child(productId)
                                    .child("bigImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    bigurl = uri.toString();
                                    saveProductToDb();
                                }
                            });
                        }
                    });
                } else if (isSmallImageChanged && !isBigImageChanged) {
                    FirebaseStorage.getInstance().getReference("products").child(productId)
                            .child("smallImage").putFile(smallUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseStorage.getInstance().getReference("products").child(productId)
                                    .child("smallImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    smallurl = uri.toString();
                                    Toast.makeText(AddProduct.this, "image", Toast.LENGTH_SHORT).show();
                                    saveProductToDb();
                                }
                            });
                        }
                    });
                } else if (isSmallImageChanged && isBigImageChanged) {
                    FirebaseStorage.getInstance().getReference("products").child(productId)
                            .child("smallImage").putFile(smallUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseStorage.getInstance().getReference("products").child(productId)
                                    .child("smallImage").getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            smallurl = uri.toString();
                                            FirebaseStorage.getInstance().getReference("products").child(productId)
                                                    .child("bigImage").putFile(bigUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            FirebaseStorage.getInstance().getReference("products").child(productId)
                                                                    .child("bigImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    bigurl = uri.toString();
                                                                    saveProductToDb();
                                                                }
                                                            });
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
                } else {
                    saveProductToDb();
                }
            } else {
                FirebaseStorage.getInstance().getReference("products").child(productId)
                        .child("smallImage").putFile(smallUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseStorage.getInstance().getReference("products").child(productId)
                                .child("smallImage").getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        smallurl = uri.toString();
                                        FirebaseStorage.getInstance().getReference("products").child(productId)
                                                .child("bigImage").putFile(bigUri)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        FirebaseStorage.getInstance().getReference("products").child(productId)
                                                                .child("bigImage").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                bigurl = uri.toString();
                                                                saveProductToDb();
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                    }
                });
            }
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unit.setText(spinnerUnitArr[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}