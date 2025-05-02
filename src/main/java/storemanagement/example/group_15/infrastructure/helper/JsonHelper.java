package storemanagement.example.group_15.infrastructure.helper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import storemanagement.example.group_15.app.dto.response.product.ProductInCartResponseDTO;

import java.util.*;

@Component
public class JsonHelper {
    public String setConditionsAsList(List<String> conditions) {
        JSONArray jsonArray = new JSONArray(conditions);
        return jsonArray.toString();
    }

    public List<String> getConditionsAsList(String condition) {
        if (condition == null || condition.isEmpty()) {
            return new ArrayList<>();
        }

        JSONArray jsonArray = new JSONArray(condition);
        List<String> conditions = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            conditions.add(jsonArray.getString(i));
        }

        return conditions;
    }

    public String convertProductsToJsonArray(List<ProductInCartResponseDTO> products) {
        JSONArray jsonArray = new JSONArray();

        for (ProductInCartResponseDTO product : products) {
            JSONObject obj = new JSONObject();
            obj.put("id", product.getId());
            obj.put("name", product.getName());
            obj.put("desc", product.getDesc());
            obj.put("price", product.getPrice());
            obj.put("quantity", product.getQuantity());
            jsonArray.put(obj);
        }
        return jsonArray.toString();
    }
}
