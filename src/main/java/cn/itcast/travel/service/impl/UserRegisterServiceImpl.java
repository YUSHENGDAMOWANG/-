package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserRegisterDao;
import cn.itcast.travel.dao.impl.UserRegisterDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserRegisterService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserRegisterServiceImpl implements UserRegisterService {

    private UserRegisterDao userRegisterDao = new UserRegisterDaoImpl();

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1 根据用户名查询用户对象
        User u = userRegisterDao.findByUsername(user.getUsername());
        //System.out.println(u);

        // 判断u是否为null
        if (u != null) {
            //用户名存在 注册失败
            return false;
        }
        //2 保存用户信息
        //2.1设置激活码 唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        userRegisterDao.save(user);

        //3. 激活邮件发送 邮件正文
        String content = "<a href='http://localhost:8080/user/active?code=" + user.getCode() + "'>点击激活【实验例题题库账号】</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");

        return true;
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码去查询用户对象
        User user = userRegisterDao.findByCode(code);
        if (user != null) {
            //2.调用dao的修改激活状态
            userRegisterDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userRegisterDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
