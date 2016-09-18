package controllers;

import models.Transaction;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import play.libs.Json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.avaje.ebean.Ebean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class TransactionController extends Controller {

    // PUT /transactionservice/transaction/$transaction_id
    public Result putTransaction(Long id) {
        RequestBody body = request().body();
        Transaction t = Transaction.fromJson(body.asJson());
        ObjectNode jsonRes = Json.newObject();
        if(t == null) {
            return badRequest("Wrong JSON request was sent.");
        } else {
            if(t.parent_id != null){
                if(Transaction.find.byId(t.parent_id) == null) {
                    jsonRes.put("status", "parent transaction (id: " + parent_id + ") not exist.");
                    return badRequest(jsonRes);
                }
            }

            if (Transaction.find.byId(id) != null) {
                t.update();
                jsonRes.put("status", "transaction updated.");
                return ok(jsonRes);
            } else {
                t.id = id;
                Ebean.save(t);
                jsonRes.put("status", "ok");
                return ok(jsonRes);
            }
        }
    }
    

    // GET /transactionservice/transaction/$transaction_id
    public Result getTransaction(Long id) {
        Transaction t = Ebean.find(Transaction.class, id);
        if(t == null) {
            ObjectNode jsonRes = Json.newObject();
            jsonRes.put("status", "transaction (id: " + id + ") not exist.");
            return badRequest(jsonRes);
        } else {
            return ok(t.toJson());
        }
    }
    

    // GET /transactionservice/types/$type
    public static Result getTypes(String type) {
        List<Long> ids = new ArrayList<Long>();
        for(Transaction t : Transaction.findByType(type)) {
            ids.add(t.id);
        }
        return ok(Json.toJson(ids));
    }

    // GET /transactionservice/sum/$transaction_id
    public static Result getSum(Long id) {
        Transaction t = Transaction.find.byId(id);
        if(t == null) {
            ObjectNode jsonRes = Json.newObject();
            jsonRes.put("status", "transaction (id: " + id + ") not exist.");
            return badRequest(jsonRes);
        } else {
            Double sum = t.amount;

            // Add up amount till transaction parent_id = null
            HashSet<Long> ids = new HashSet<Long>();
            ids.add(t.id);
            while(t.parent_id != null) {
                t = Transaction.find.byId(t.parent_id);
                if(!ids.add(t.id)) {
                    break;
                } else {
                    sum += t.amount;
                }
            }
            ObjectNode jsonRes = Json.newObject();
            jsonRes.put("sum", sum);
            return ok(jsonRes);
        }
    }
}
