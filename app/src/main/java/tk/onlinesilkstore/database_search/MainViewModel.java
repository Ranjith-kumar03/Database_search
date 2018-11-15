package tk.onlinesilkstore.database_search;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private ProductRepository repository;
    private LiveData<List<Product>> allProducts;
    private MutableLiveData<List<Product>> searchResults;

    public MainViewModel (Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllProducts();
        searchResults = repository.getSearchResults();
    }

    MutableLiveData<List<Product>> getSearchResults() {
        return searchResults;
    }

    LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insertProduct(Product product) {
        repository.insertProduct(product);
    }

    public void findProduct(String name) {
        repository.findProduct(name);
    }

    public void deleteProduct(String name) {
        repository.deleteProduct(name);
    }

}
