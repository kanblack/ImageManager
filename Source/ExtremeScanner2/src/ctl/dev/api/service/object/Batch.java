
package ctl.dev.api.service.object;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Batch {

	
	
    public Batch(String id, String name, List<Product> products) {
		super();
		this.id = id;
		this.name = name;
		this.products = products;
	}

	@Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private List<Product> products = new ArrayList<Product>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
