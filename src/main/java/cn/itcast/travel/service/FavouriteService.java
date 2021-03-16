package cn.itcast.travel.service;

public interface FavouriteService {
    public  boolean isFavourite(String rid,int uid);

    public  void add(String rid, int uid);
}
