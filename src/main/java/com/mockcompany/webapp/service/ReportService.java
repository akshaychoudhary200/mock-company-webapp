package com.mockcompany.webapp.service;

import com.mockcompany.webapp.api.SearchReportResponse;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface ReportService {
    public SearchReportResponse runReport();
}

@Service
class ReportServiceImple implements ReportService {

    private final EntityManager entityManager;

    @Autowired
    public ReportServiceImple(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public SearchReportResponse runReport() {
        Map<String, Integer> hits = new HashMap<>();
        SearchReportResponse response = new SearchReportResponse();
        response.setSearchTermHits(hits);

        int count = this.entityManager.createQuery("SELECT item FROM ProductItem item").getResultList().size();

        List<Number> matchingIds = new ArrayList<>();
        matchingIds.addAll(
                this.entityManager.createQuery("SELECT item.id from ProductItem item where item.name like '%cool%'").getResultList()
        );
        matchingIds.addAll(
                this.entityManager.createQuery("SELECT item.id from ProductItem item where item.description like '%cool%'").getResultList()
        );
        matchingIds.addAll(
                this.entityManager.createQuery("SELECT item.id from ProductItem item where item.name like '%Cool%'").getResultList()
        );
        matchingIds.addAll(
                this.entityManager.createQuery("SELECT item.id from ProductItem item where item.description like '%cool%'").getResultList()
        );
        List<Number> counted = new ArrayList<>();
        for (Number id: matchingIds) {
            if (!counted.contains(id)) {
                counted.add(id);
            }
        }

        response.getSearchTermHits().put("Cool", counted.size());


        response.setProductCount(count);

        List<ProductItem> allItems = entityManager.createQuery("SELECT item FROM ProductItem item").getResultList();
        int kidCount = 0;
        int perfectCount = 0;
        Pattern kidPattern = Pattern.compile("(.*)[kK][iI][dD][sS](.*)");
        for (ProductItem item : allItems) {
            if (kidPattern.matcher(item.getName()).matches() || kidPattern.matcher(item.getDescription()).matches()) {
                kidCount += 1;
            }
            if (item.getName().toLowerCase().contains("perfect") || item.getDescription().toLowerCase().contains("perfect")) {
                perfectCount += 1;
            }
        }
        response.getSearchTermHits().put("Kids", kidCount);

        response.getSearchTermHits().put("Amazing", entityManager.createQuery("SELECT item FROM ProductItem item where lower(concat(item.name, ' - ', item.description)) like '%amazing%'").getResultList().size());

        hits.put("Perfect", perfectCount);

        return response;
    }
}