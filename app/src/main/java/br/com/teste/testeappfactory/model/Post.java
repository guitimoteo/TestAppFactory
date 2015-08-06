package br.com.teste.testeappfactory.model;

public class Post {
    int userId;
    int id;
    String title;
    String body;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        if(title == null)
            throw new NullPointerException("Params should not be null");
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        if(body == null)
            throw new NullPointerException("Params should not be null");
        this.body = body;
    }
}
