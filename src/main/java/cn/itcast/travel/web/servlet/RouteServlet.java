package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.enums.ResultTypeEnum;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.FavouriteService;
import cn.itcast.travel.service.JavaComplieService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import cn.itcast.travel.service.impl.FavouriteServiceImpl;
import cn.itcast.travel.service.impl.JavaComplieServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import cn.itcast.travel.vo.ResultResponse;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private JavaComplieService javaComplieService = new JavaComplieServiceImpl();

    private RouteService routeService = new RouteServiceImpl();
    private FavouriteService favouriteService = new FavouriteServiceImpl();
    private CategoryService categoryService = new CategoryServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接受rname 线路名称
        String rname = request.getParameter("rname");


        int cid = 0;//类别id
        //2.处理参数
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //3. 调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);

        //4. 将pageBean对象序列化为json，返回
        writeValue(pb, response);

    }

    public void pageQueryNull(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        int cid = 0;//类别id
        //2.处理参数
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //3. 调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQueryNull(cid, currentPage, pageSize);

        //4. 将pageBean对象序列化为json，返回
        writeValue(pb, response);

    }

    /**
     * 根据id查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受id
        String rid = request.getParameter("rid");
        System.out.println("rid:" + rid);
        //调用service查询route对象
        Route route = routeService.findOne(rid);

        System.out.println("route:" + route);

        //转为json写回客户端
        writeValue(route, response);

    }

    /**
     * 判断当前用户是否收藏过该路线
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavourite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取线路id
        String rid = request.getParameter("rid");
        //获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用户未登录
            uid = 0;

        } else {
            //用户登录
            uid = user.getUid();

        }
        //调用FavouriteService查询是否收藏
        boolean flag = favouriteService.isFavourite(rid, uid);
        //写回客户端
        writeValue(flag, response);
    }

    public void addFavourite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取线路id
        String rid = request.getParameter("rid");
        //获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //用户未登录
            return;

        } else {
            //用户登录
            uid = user.getUid();

        }

        //调用service添加
        favouriteService.add(rid, uid);
    }

    /**
     * 获取导航栏上一级
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageCateGory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        Route one = routeService.findOne(rid);
        int cid = one.getCid();
        String cname = categoryService.findOne(cid);
        ArrayList<String> context = new ArrayList<>();
        context.add(String.valueOf(cid));
        context.add(cname);

        writeValue(context, response);


    }

    /**
     * 在线编译程序
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void complie(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String javaSource = request.getParameter("javaSource");
        System.out.println("javaSource" + javaSource);
        try {
            if (StringUtils.isEmpty(javaSource)) {
                writeValue(ResultResponse.Build(ResultTypeEnum.error, "代码不能为空！"), response);
            }
            Class clazz = javaComplieService.complie(javaSource);
            writeValue(javaComplieService.excuteMainMethod(clazz), response);
        } catch (Exception e) {
            e.printStackTrace();
            writeValue(ResultResponse.Build(ResultTypeEnum.error, "编译出错了！ 错误信息:" + e.getMessage()), response);

        }
    }


    /**
     * 获取运行时程序需要的参数
     *
     * @param excuteArgsStr
     * @return
     */
    private String[] getInputArgs(String excuteArgsStr) {
        if (StringUtils.isEmpty(excuteArgsStr)) {
            return null;
        } else {
            return excuteArgsStr.split(" ");
        }
    }


}


