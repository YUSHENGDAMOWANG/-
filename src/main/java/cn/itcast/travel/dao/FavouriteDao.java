package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavouriteDao {
    public Favorite findByRidAndUid(int rid, int uid);

    public int findCountByRid(int rid);

    public void add(int rid, int uid);
}
