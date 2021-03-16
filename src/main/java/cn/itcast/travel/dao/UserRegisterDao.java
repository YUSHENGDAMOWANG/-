package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserRegisterDao {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * 用户保存
     * @param user
     */

    public void save(User user);

    /**
     * 根据激活码查询对象
     * @param code
     * @return
     */
    public User findByCode(String code);

    /**
     * 根据用户更改激活状态
     * @param user
     */
    public void updateStatus(User user);

    /**
     * 根据用户名和密码实现用户的登录查询
     * @param username
     * @param password
     * @return
     */
    public User findByUsernameAndPassword(String username, String password);
}
