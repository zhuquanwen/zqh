package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Employee;
import com.zqw.order.manage.domain.p.UrlPath;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.BasePageResult;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.entity.ResponseEntity;
import com.zqw.order.manage.service.api.ClothSizeService;
import com.zqw.order.manage.service.api.EmployeeService;
import com.zqw.order.manage.service.api.StyleService;
import com.zqw.order.manage.service.api.UrlPathService;
import com.zqw.order.manage.util.UrlPathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController extends BaseController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ClothSizeService clothSizeService;
    @Autowired
    private UrlPathService urlPathService;

    @Autowired
    private StyleService styleService;
    @GetMapping("/employee")
    public ModelAndView turnGoods(HttpServletRequest request, HttpSession session) throws PageException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(this.validateSession(request,session,"employeePage",clothSizeService,styleService));
        mav.addObject(HIDDEN_FLAG, "employee");
        return mav;
    }



    @PostMapping(value = "/employee")
    @ResponseBody
    public BasePageResult<Employee> goods(HttpSession session, Employee employee) throws AjaxException {
        BasePageResult bpr = new BasePageResult();
        try {

            if (employee.getDirection() == null) {
                employee.setDirection("asc");
            }
            if (employee.getField() == null) {
                employee.setField("id");
            }
            Sort sort = new Sort(employee.getDirection(), employee.getField());
            Pageable pageable = new PageRequest(employee.getPage(), employee.getSize(), sort);

            Page<Employee> page = employeeService.findByPageAndParams(employee, pageable);
            List<Employee> employeeList = page.getContent();
            employeeList.forEach(employee1 -> {
                List<UrlPath> urlPathList = employee1.getUrlPathList();
                if(urlPathList != null && urlPathList.size() != 0){
                    for (UrlPath urlPath: urlPathList) {
                        String flag = urlPath.getUseFlag();
                        if("1".equals(flag)){
                            employee1.setUrlPath(urlPath.getPath());
                        }
                    }
                }
            });
            bpr.setResult(page.getContent());
            bpr.setTotalCount(page.getTotalElements());
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }
        return bpr;
    }

    @PostMapping(value = "/employee/save")
    public @ResponseBody ResponseEntity saveOrder(Employee employee){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            //生成一个地址标识
            if(employee.getId() == null){
                UrlPath urlPath = new UrlPath();
                urlPath.setPath(UrlPathUtils.createNewPath(employee.getName()));
                urlPath.setUseFlag("1");
                List<UrlPath> urlPathList = new ArrayList<UrlPath>();
                urlPathList.add(urlPath);
                employee.setUrlPathList(urlPathList);
                employee = employeeService.save(employee);
                urlPath.setEmployee(employee);
                urlPathService.save(urlPath);
            }else{
                employee = employeeService.save(employee);
            }

//            urlPath.setEmployee(employee);


            re.setData(employee);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

    @GetMapping(value = "/employee/{id}")
    public @ResponseBody ResponseEntity getOrderById(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Employee employee = employeeService.findOne(id);
            re.setData(employee);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
    @GetMapping(value = "/employee/urlPath/{id}/{flag}/{pathId}")
    public @ResponseBody ResponseEntity getEmployeeUrlPath(@PathVariable Long id,@PathVariable String flag,
                                                           @PathVariable Long pathId){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Employee employee = employeeService.findOne(id);
            List<UrlPath> urlPathList = employee.getUrlPathList();
            if("add".equals(flag)){
                //新增一个地址，并将它变为1
                UrlPath urlPath = new UrlPath();
//                urlPath.setEmployee(employee);
                urlPath.setUseFlag("1");
                urlPath.setPath(UrlPathUtils.createNewPath(employee.getName()));
                urlPathList.forEach( urlPath1 -> {
                    urlPath1.setUseFlag("0");
                });
                urlPathList.add(urlPath);
//                employee = employeeService.save(employee);
                urlPath.setEmployee(employee);
                urlPath = urlPathService.save(urlPath);
            }else if("edit".equals(flag)){

            }else if("change".equals(flag)){
                urlPathList.forEach(urlPath -> {
                    urlPath.setUseFlag("0");
                    if(pathId == urlPath.getId()){
                        urlPath.setUseFlag("1");
                    }

                });
                urlPathList = urlPathService.save(urlPathList);

            }else if("delete".equals(flag)){
                for (int j = urlPathList.size() - 1 ; j >= 0; j--){
                    UrlPath urlPath = urlPathList.get(j);
                    if(pathId == urlPath.getId()){
                        urlPathList.remove(j);
                        urlPathService.delete(urlPath);
                        break;
                    }
                }
//                urlPathList = urlPathService.save(urlPathList);
            }

//            employee = employeeService.findOne(id);
//            urlPathList = employee.getUrlPathList();
            Map<String,String> map = new HashMap<String,String>();
            map.put("name",employee.getName());
            if(urlPathList != null && urlPathList.size() > 0){
                String html = "";
                /*<li class="list-group-item">免费域名注册</li>*/
                for (UrlPath urlPath: urlPathList) {

                    html += "<li class='list-group-item'>";
                    html += "<input type='radio'  ";
                    if("1".equals(urlPath.getUseFlag())){
                        html += " checked='checked' ";
                    }else{
                        html += " onclick='changeAddress(" + urlPath.getId() + ")' ";
                    }
                    html += "/>";
                    html += urlPath.getPath();
                    if("1".equals(urlPath.getUseFlag())){
                        html += " <img \"style='width:12px;height: 12px;' src='image/use.png'/> ";
                    }else{
                        html += " <img \"style='width:12px;height: 12px;' src='image/unuse.png' onclick='changeAddress(" + urlPath.getId() + ")'/> ";
                        html += " <img \"style='width:12px;height: 12px;float:right;' src='image/delete_min.png' onclick='deleteAddress(" + urlPath.getId() + ")' /> ";
                    }

                    html += "</li>";
                }
                map.put("lis",html);
            }else {
                map.put("lis","");

            }
            re.setData(map);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }

    @GetMapping(value = "/employee/delete/{id}")
    public @ResponseBody ResponseEntity deleteOrder(@PathVariable Long id){
        ResponseEntity re = new ResponseEntity(HttpStatus.OK.value(),"操作成功");
        try{
            Employee employee = employeeService.findOne(id);
            employeeService.delete(employee);
            re.setData(null);
        }catch (Exception e){
            e.printStackTrace();
            re.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            re.setInfo("操作出错");
        }
        return re;
    }
}
