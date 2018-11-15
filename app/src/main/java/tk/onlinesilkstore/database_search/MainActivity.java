package tk.onlinesilkstore.database_search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;
    private ProductListAdapter adapter;

    private TextView productId;
    private EditText productName;
    private EditText productQuantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        productId = findViewById(R.id.productID);
        productName = findViewById(R.id.productName);
        productQuantity = findViewById(R.id.productQuantity);

        listenerSetup();
        observerSetup();
        recyclerSetup();
    }

    private void clearFields() {
        productId.setText("");
        productName.setText("");
        productQuantity.setText("");
    }
    private void listenerSetup() {

        Button addButton = findViewById(R.id.addButton);
        Button findButton = findViewById(R.id.findButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = productName.getText().toString();
                String quantity = productQuantity.getText().toString();

                if (!name.equals("") && !quantity.equals("")) {
                    Product product = new Product(name,
                            Integer.parseInt(quantity));
                    mViewModel.insertProduct(product);
                    clearFields();
                } else {
                    productId.setText("Incomplete information");
                }
            }
        });

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.findProduct(productName.getText().toString());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.deleteProduct(productName.getText().toString());
                clearFields();
            }
        });
    }

    private void observerSetup() {

        mViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable final List<Product> products) {
                adapter.setProductList(products);
            }
        });

        mViewModel.getSearchResults().observe(this,
                new Observer<List<Product>>() {
                    @Override
                    public void onChanged(@Nullable final List<Product> products) {

                        if (products.size() > 0) {
                            productId.setText(String.format(Locale.US, "%d",
                                    products.get(0).getId()));
                            productName.setText(products.get(0).getName());
                            productQuantity.setText(String.format(Locale.US, "%d",
                                    products.get(0).getQuantity()));
                        } else {
                            productId.setText("No Match");
                        }
                    }
                });
    }
    private void recyclerSetup() {

        RecyclerView recyclerView;

        adapter = new ProductListAdapter(R.layout.product_list_item);
        recyclerView = findViewById(R.id.product_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}



