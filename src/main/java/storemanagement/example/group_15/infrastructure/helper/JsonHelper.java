package storemanagement.example.group_15.infrastructure.helper;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JsonHelper {
    public String setConditionsAsList(List<String> conditions) {
        JSONArray jsonArray = new JSONArray(conditions);
        return  jsonArray.toString();
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
}
