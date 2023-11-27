package study.coco.model;

public class Book {
    public String name;
    public String author;
    public int publishedYear;
    public int page;

/*
    public Book (String name, String author, int publishedYear, int page){
        this (name, author, publishedYear, page);
    }
*/
    public Book (String name, String author, int publishedYear, int page){
        this.name = name;
        this.author = author;
        this.publishedYear = publishedYear;
        this.page = page;
    }

    public void title(){
        System.out.println("Title: "+this.name);
    }

}
