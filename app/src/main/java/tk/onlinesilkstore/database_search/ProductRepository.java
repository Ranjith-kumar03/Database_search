package tk.onlinesilkstore.database_search;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;

public class ProductRepository implements AsyncResult {

    private LiveData<List<Product>> allProducts;
    private MutableLiveData<List<Product>> searchResults =
            new MutableLiveData<>();


    private ProductDao productDao;

    public ProductRepository(Application application) {

        ProductRoomDatabase db;
        db = ProductRoomDatabase.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProducts();
    }




    @Override
    public void asyncFinished(List<Product> results){
        searchResults.setValue(results);
    }

    ///////////DAO Request

    public void insertProduct(Product newproduct) {
        new queryAsyncTask.insertAsyncTask(productDao).execute(newproduct);
    }

    public void deleteProduct(String name) {
        new queryAsyncTask.deleteAsyncTask(productDao).execute(name);
    }

    public void findProduct(String name) {
        queryAsyncTask task = new queryAsyncTask(productDao);
        task.delegate = this;
        task.execute(name);
    }


    //Asynch Task Starts Here
    private static class queryAsyncTask extends AsyncTask<String, Void, List<Product>> {

        private ProductDao asyncTaskDao;
        private ProductRepository delegate = null;

        queryAsyncTask(ProductDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<Product> doInBackground(final String... params) {
            return asyncTaskDao.findProduct(params[0]);
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            delegate.asyncFinished(result);
        }

        private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {

            private ProductDao asyncTaskDao;

            insertAsyncTask(ProductDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final Product... params) {
                asyncTaskDao.insertProduct(params[0]);
                return null;
            }
        }

        private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

            private ProductDao asyncTaskDao;

            deleteAsyncTask(ProductDao dao) {
                asyncTaskDao = dao;
            }

            @Override
            protected Void doInBackground(final String... params) {
                asyncTaskDao.deleteProduct(params[0]);
                return null;
            }
        }

    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public MutableLiveData<List<Product>> getSearchResults() {
        return searchResults;
    }
}