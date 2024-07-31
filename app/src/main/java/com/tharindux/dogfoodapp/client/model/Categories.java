package com.tharindux.dogfoodapp.client.model;

public class Categories {
    private String name;

    private String CategoryPic;

    public Categories(){

    }
    public Categories(String name, String categoryPic) {
        this.name = name;
        this.CategoryPic = categoryPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryPic() {
        return CategoryPic;
    }


    public void setCategoryPic(String categoryPic) {
        CategoryPic = categoryPic;
    }

//    public static ArrayList<Categories> getSampleCategoryList(){
//
//        ArrayList<Categories> sampleCategoryList= new ArrayList<>();
//        sampleCategoryList.add(new Categories(1,"All"));
//        sampleCategoryList.add(new Categories(2,"Milo"));
//        sampleCategoryList.add(new Categories(4,"Chipps"));
//        sampleCategoryList.add(new Categories(5,"dakoo"));
//        sampleCategoryList.add(new Categories(6,"salt"));
//        sampleCategoryList.add(new Categories(7,"cheess"));
//        sampleCategoryList.add(new Categories(8,"halapa"));
//        return sampleCategoryList;
//    }

}
