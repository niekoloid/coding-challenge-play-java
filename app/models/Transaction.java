package models;

import java.util.List;
import javax.persistence.*;

import play.data.validation.Constraints.*;
import com.avaje.ebean.*;
import play.libs.Json;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
public class Transaction extends Model{

	// Define the fields
    @Id
    @Required
    public Long id = null;

    @Required
    public Double amount = null;

    @Required
    public String type = null;

    public Long parent_id = null;

    // Constructors
    public Transaction(Double amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public Transaction(Double amount, String type, Long parent_id) {
        this.amount = amount;
        this.type = type;
        this.parent_id = parent_id;
    }

    // Define methods
    public static JsonNode toJson() {
        return Json.toJson(this);
    }

    public static Transaction fromJson(JsonNode json) {

	    try {
	        return Json.fromJson(json, Transaction.class);
	    } catch (Exception e) {
	        System.out.println("Exception Error: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }

    }

	public static List<Transaction> findByType(String type) {
        return find.where().ieq("type", type).findList();
    }

	// Define Finder as find method
    public static Finder<Long,Transaction> find = new Finder<Long,Transaction>(Long.class, Transaction.class);
}