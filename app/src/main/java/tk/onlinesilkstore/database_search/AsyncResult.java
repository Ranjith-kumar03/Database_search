package tk.onlinesilkstore.database_search;

import java.util.List;

public interface AsyncResult {
    void asyncFinished(List<Product> results);
}