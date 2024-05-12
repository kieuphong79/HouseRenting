package com.uet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uet.model.Address;
import com.uet.model.HouseType;
import com.uet.model.SearchParameter;

public class SearchTest {
    @Test
    public void searchTest() {
        SearchParameter p =new SearchParameter();
        String actual = createSQL(p, 0, 10);
        String expected = "select * from houses where isPublic = 1 ORDER BY requiringDate DESC, id ASC LIMIT 0,10;";
        assertEquals("non parameter", actual, expected);

        p = new SearchParameter();
        p.setKeyWord("search key word");
        actual = createSQL(p, 0, 0);
        expected = "select * from houses where match(title) against (?) and isPublic = 1 LIMIT 0,0;";
        assertEquals("have keyWord parameter", actual, expected);
        
        p = new SearchParameter();
        p.setAddress(new Address("ad1", "ad2", "ad3", "ad4"));
        actual = createSQL(p, 0, 0);
        expected = "select * from houses where city = 'ad2' and district = 'ad3' and street = 'ad4' and isPublic = 1 ORDER BY requiringDate DESC, id ASC LIMIT 0,0;";
        assertEquals("have keyWord parameter", actual, expected);

        p = new SearchParameter();
        p.setAddress(new Address("ad1", "ad2", "ad3", "ad4"));
        actual = createSQL(p, 0, 0);
        expected = "select * from houses where city = 'ad2' and district = 'ad3' and street = 'ad4' and isPublic = 1 ORDER BY requiringDate DESC, id ASC LIMIT 0,0;";
        assertEquals("have keyWord parameter", actual, expected);
    }
    public String createSQL(SearchParameter curParameter, int offset, int limit) {
        boolean hasKeyword = false;
        StringBuffer temp = new StringBuffer("select * from houses ");
        boolean needAND = false;
        if (!curParameter.getKeyWord().isEmpty())  {
            hasKeyword = true;
            temp.append("where ");
            // temp.append("match(title, description) against (?) ");
            temp.append("match(title) against (?) ");
            needAND = true;
        }
        if(curParameter.getTypeOfHouse() != HouseType.ALL) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            needAND = true;
            if (curParameter.getTypeOfHouse() == HouseType.HOUSE_LAND) {
                temp.append("houseType = 'HOUSE_LAND' ");
            } else if (curParameter.getTypeOfHouse() == HouseType.BEDSIT) {
                temp.append("houseType = 'BEDSIT' ");
            } else if (curParameter.getTypeOfHouse() == HouseType.APARTMENT) {
                temp.append("houseType = 'APARTMENT' ");
            }
        }
        if (!curParameter.getAddress().getCity().equals("Tất cả")) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("city = '").append(curParameter.getAddress().getCity()).append("' ");
            needAND = true;
        } 
        if (!curParameter.getAddress().getDistrict().equals("Tất cả")) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("district = '").append(curParameter.getAddress().getDistrict()).append("' ");
            needAND = true;
        } 
        if (!curParameter.getAddress().getStreet().equals("Tất cả")) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("street = '").append(curParameter.getAddress().getStreet()).append("' ");
            needAND = true;
        } 
        boolean lowPrice = !curParameter.getLowerBoundPrice().isEmpty();
        boolean highPrice = !curParameter.getUpperBoundPrice().isEmpty();
        if (lowPrice & highPrice) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("price between ").append(getMillions(curParameter.getLowerBoundPrice())).append(" and ").append(getMillions(curParameter.getUpperBoundPrice())).append(" ");
            needAND = true;
        } else if (lowPrice) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("price >= ").append(getMillions(curParameter.getLowerBoundPrice())).append(" ");
            needAND = true;
        } else if (highPrice) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("price <= ").append(getMillions(curParameter.getUpperBoundPrice())).append(" ");
            needAND = true;
        }
        String numberOfBedrooms = curParameter.getNumOfBedrooms().split(" ")[0];
        if (!curParameter.getNumOfBedrooms().equals("Tất cả")) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            if (numberOfBedrooms.equals("4+")) {
                temp.append("numberOfBedrooms >= 4 ");
            } else temp.append("numberOfBedrooms = ").append(numberOfBedrooms).append(" ");
            needAND = true;
        } 
        boolean lowArea = !curParameter.getLowerBoundArea().isEmpty();
        boolean highArea = !curParameter.getUpperBoundArea().isEmpty();
        if (lowArea & highArea) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("area between ").append(curParameter.getLowerBoundArea()).append(" and ").append(curParameter.getUpperBoundArea()).append(" ");
            needAND = true;
        } else if (lowArea) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("area >= ").append(curParameter.getLowerBoundArea()).append(" ");
            needAND = true;
        } else if (highArea) {
            if (needAND) temp.append("and ");
            else temp.append("where ");
            temp.append("area <= ").append(curParameter.getUpperBoundArea()).append(" ");
            needAND = true;
        }
        if (needAND) temp.append("and ");
        else temp.append("where ");
        if (hasKeyword) {
            temp.append("isPublic = 1");
        } else temp.append("isPublic = 1 ORDER BY requiringDate DESC, id ASC");
                        
        String countSt = temp.toString() + ";";
        temp.append(" LIMIT ").append(String.valueOf(offset)).append(",").append(limit).append(";");
        return temp.toString();
    }
    private String getMillions(String s) {
            return String.valueOf(Integer.valueOf(s) * 1000000);
        }


    
    
}
