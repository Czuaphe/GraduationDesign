package com.graduation.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.beust.jcommander.internal.Lists;
import com.graduation.dao.AdminDao;
import com.graduation.dao.MajorDao;
import com.graduation.dao.ProblemDao;
import com.graduation.dao.SelectedDao;
import com.graduation.dao.StudentDao;
import com.graduation.dao.TeacherDao;
import com.graduation.entity.Admin;
import com.graduation.entity.Major;
import com.graduation.entity.Problem;
import com.graduation.entity.Selected;
import com.graduation.entity.Student;
import com.graduation.entity.Teacher;

public class ProblemService {

	private ProblemDao problemDao = new ProblemDao();
	private StudentDao studentDao = new StudentDao();
	private SelectedDao selectedDao = new SelectedDao();
	private MajorDao majorDao = new MajorDao();
	private TeacherDao teacherDao = new TeacherDao();

	private List<String> pathList = new ArrayList<>();

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	private JSONObject jsonObjectOutput = new JSONObject();

	public JSONObject getJsonObjectOutput() {
		return jsonObjectOutput;
	}

	public ProblemService() {
	}

	public ProblemService(List<String> pathList, HttpServletRequest request) {
		this();
		this.pathList = pathList;
		this.request = request;
	}

	public ProblemService(List<String> pathList, HttpServletRequest request,
			HttpServletResponse response) {
		this(pathList, request);
		this.response = response;
	}

	public JSONObject redirectToPath() {

		// TODO 插入一个用户
		HttpSession session = request.getSession();

		jsonObjectOutput = new JSONObject();
		// 判断二级路由
		switch (pathList.get(1)) {
		case "add":
			// 添加一个课题
			String[] info = request.getParameterValues("info[]");
			addProblem(info);
			break;
		case "show":
			// 使用三级路由
			// 显示课题的基本信息
			show();
			break;
		case "dels":
			// 删除课题
			String[] delList = request.getParameterValues("ids[]");
			deleteProblem(delList);
			break;
		case "details":
			// 显示课题的详细信息
			details();
			break;
		case "update":
			List<String[]> updateStringList = new ArrayList<>();
			int num = 0;
			while (true) {
				String infos = "infos[" + (num++) + "][]";
				String[] updateList = request.getParameterValues(infos);
				if (updateList != null) {
					updateStringList.add(updateList);
				} else {
					break;
				}
			}
			updateProblem(updateStringList);
			break;
		case "selected":
			// 使用三级路由
			// 根据用户来得到选题信息
			selected();
			break;
		case "verify":
			// 审核课题
			verifyProblem();
			break;
		case "export":
			// 使用三级路由
			// 根据用户得到课题的信息
			export();
			break;
		default:
			System.out.println("二级路由无法解析！！！");
			break;
		}

		return jsonObjectOutput;

	}

	/**
	 * 添加课题
	 * 
	 * @param info
	 *            课题的字符串数组信息
	 */
	public void addProblem(String[] info) {

		jsonObjectOutput = new JSONObject();
		
		HttpSession session = request.getSession();
		String actError =null; 
		Object actObject = session.getAttribute("act");
		if (actObject == null) {
			actError = "没有登录！！不能显示课题信息";
			System.out.println(actError);
			return;
		}
		int act = Integer.parseInt(String.valueOf(actObject));
		System.out.println("act : " + act);
		if (act != 2 && act != 4) {
			actError = "登录用户错误";
			System.out.println(actError);
			return ;
		}
		
		Teacher teacher = (Teacher) session.getAttribute("user");
		
		List<String> list = new ArrayList<>();
		for (String string : info) {
			list.add(string);
		}
		
		// 检测数据的正确性
		String error = checkProblem(list);

		if (error != null) {

			jsonObjectOutput.put("status", false);
			jsonObjectOutput.put("info", error);

		} else {
			
			// 数据转换成对象
			Problem problem = toBeanAdd(list);
			// 设置教师ID
			problem.setTea_id(teacher.getTea_id());
			
			// 保存到数据库中
			boolean b = problemDao.save(problem);

			// 将返回的结果保存到JSON中
			jsonObjectOutput.put("status", b);
			if (!b) {
				jsonObjectOutput.put("error", "未知原因保存失败！");
			}

		}

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据不同的用户显示不同的课题信息
	 */
	public void show() {

		HttpSession session = request.getSession();
		String error = null;

		// 检测用户是否登录
		Object actObject = session.getAttribute("act");
		if (actObject == null) {
			error = "没有登录！！不能显示课题信息";
			System.out.println(error);
			return;
		}

		// 如果没有三级路由，就使用默认函数
		if (pathList.size() < 3) {
			showProblem();
			return;
		}

		switch (pathList.get(2)) {
		case "":
			showProblem();
			break;
		case "major":
			showMajor();
			break;
		case "teacher":
			showTeacher();
			break;
		// case "details":
		// showDetails();
		// break;
		default:
			break;
		}

	}

	/**
	 * 显示当前页的数据，管理员使用
	 * 
	 * @param page
	 *            页码
	 */
	public void showProblem() {

		jsonObjectOutput = new JSONObject();

		// 得到要显示的页数
		int page = Integer.parseInt(request.getParameter("currentPage"));

		boolean flag = false;

		int pageSize = 5;
		// 得到当前页的所有对象
		List<Problem> list = problemDao.queryByPage(page, pageSize);

		if (list != null) {
			flag = true;
		}

		// 课题的总数量
		long count = problemDao.queryCount();

		JSONArray jsonList = new JSONArray();
		// 将对象转换成 JSON 数据
		for (Problem problem : list) {

			List<Object> objectList = toObjectShow(problem);

			jsonList.add(objectList);
		}
		// 返回所有专业
		jsonObjectOutput.put("majors", getAllMajors());

		// 设置要返回的课题数据
		jsonObjectOutput.put("data", jsonList);
		// 设置当前页
		jsonObjectOutput.put("currentPage", page);
		// 设置返回状态
		jsonObjectOutput.put("status", flag);
		// 设置总页数
		jsonObjectOutput.put("totalPage", (count - 1) / pageSize + 1);

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 显示一个专业的课题信息，学生和专业负责人使用
	 */
	public void showMajor() {

		jsonObjectOutput = new JSONObject();

		System.out.println("正在显示一个专业的课题信息");

		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "没有登录！！不能显示课题信息";
			System.out.println(error);
			return;
		}

		Integer act = Integer.parseInt(String.valueOf(actObject));
		// 如果是教师或管理员登录，则直接退出
		if (act == 2 || act == 3) {
			error = "登录用户错误，无法得到一个专业的课题信息。";
			System.out.println(error);
			return;
		}

		// 存放一页课题信息的JSON
		JSONArray problemArray = new JSONArray();

		List<Problem> problemList = null;
		long count = 0; // 一个专业中的总课题数
		int page = 0; // 一页的课题数
		int pageSize = 5;

		if (act == 1) {

			System.out.println("学生登录！！");
			// 学生用户登录
			Student student = (Student) session.getAttribute("user");
			// 得到页数
			page = Integer.parseInt(request.getParameter("currentPage"));
			System.out.println("page:" + page);

			// 判断学生是否指定题目
			List<Problem> wayList = problemDao.queryByWay(student.getStu_id());
			System.out.println("stu_id:" + student.getStu_id());
			if (wayList != null && wayList.size() != 0) {
				problemList = wayList;
				count = 1;

			} else {
				// 通过学生的专业得到所有盲选题目
				problemList = problemDao.queryByMIDPage(student.getMid(), page,
						pageSize);
				count = problemDao.queryByMIDCount(student.getMid());
			}
			System.out.println("指定学生的题目数量：" + wayList.size());
			System.out.println("总数量：" + count);
			// 开始生成JSON数据
			for (Problem problem : problemList) {
				JSONArray objectArray = new JSONArray();
				List<Object> objectList = toObjectShowMajor4Student(problem);
				for (Object object : objectList) {
					objectArray.add(object);
				}
				problemArray.add(objectArray);
			}

		} else if (act == 4) {
			// 专业负责人登录
			// 得到页数
			page = Integer.parseInt(request.getParameter("currentPage"));

			Teacher teacher = (Teacher) session.getAttribute("user");
			int mid = majorDao.queryByTea_id(teacher.getTea_id()).getMid();

			problemList = problemDao.queryByMIDPage(mid, page, pageSize);

			count = problemDao.queryByMIDCount(mid);
			// TODO 专业负责人得到一个专业所有课题信息

		}

		// 将专业中的一页数据转换成JSON数据
		jsonObjectOutput.put("data", problemArray);
		jsonObjectOutput.put("currentPage", page);
		jsonObjectOutput.put("status", true);
		jsonObjectOutput.put("totalPage", (count - 1) / pageSize + 1);

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 显示一个教师的课题信息，教师和专业负责人使用
	 */
	public void showTeacher() {

		System.out.println("正在显示教师课题信息！！");

		jsonObjectOutput = new JSONObject();

		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "没有登录！！不能显示课题信息";
			System.out.println(error);
			return;
		}

		Integer act = Integer.parseInt(String.valueOf(actObject));
		System.out.println("act:" + act);
		// 如果不是教师也不是专业负责人登录，则直接退出
		if (act != 2 && act != 4) {
			error = "登录用户错误，无法得到一个专业的课题信息。";
			System.out.println("error:" + error);
			return;
		}

		Teacher teacher = (Teacher) session.getAttribute("user");
		// System.out.println("Teacher info:" + teacher.toString());
		List<Problem> problemList = problemDao.queryByTea_id(teacher
				.getTea_id());

		System.out.println("该教师拥有的课题数量为：" + problemList.size());

		JSONArray problemArray = new JSONArray();

		for (Problem problem : problemList) {
			JSONArray objectArray = new JSONArray();
			List<Object> objectList = toObjectShow(problem);
			for (Object object : objectList) {
				objectArray.add(object);
			}
			problemArray.add(objectArray);
		}

		jsonObjectOutput.put("data", problemArray);
		jsonObjectOutput.put("status", true);

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	public void showDetails() {

		jsonObjectOutput = new JSONObject();

		String idString = request.getParameter("pro_id");

		int problem_id = Integer.parseInt(idString);

		Problem problem = problemDao.queryByProblem_id(problem_id);

		if (problem == null) {
			jsonObjectOutput.put("status", false);
		} else {

			List<Object> objectList = toObjectDetailsTeacher(problem);

			JSONArray objectArray = new JSONArray();

			for (Object object : objectList) {
				objectArray.add(object);
			}

			jsonObjectOutput.put("status", true);

			jsonObjectOutput.put("info", objectArray);
		}

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 删除课题
	 * 
	 * @param list
	 *            要删除的课题的ID的列表
	 */
	public void deleteProblem(String[] list) {

		jsonObjectOutput = new JSONObject();

		if (list.length != 0) {

			List<Integer> delList = new ArrayList<>();

			for (String string : list) {
				delList.add(Integer.parseInt(string));
			}

			List<Boolean> delFlagList = new ArrayList<>();

			for (Integer problem_id : delList) {
				boolean flag = problemDao.remove(problem_id);
				System.out.println("delete id: " + problem_id + ",result: "
						+ flag);
				delFlagList.add(flag);
			}

			JSONArray delJsonArray = new JSONArray();

			for (int i = 0; i < delFlagList.size(); i++) {
				if (delFlagList.get(i)) {
					delJsonArray.add(delList.get(i));
				}

			}

			jsonObjectOutput.put("status", true);
			// 设置返回删除成功的课题的ID
			jsonObjectOutput.put("ids", delJsonArray);

		}

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void details() {
		HttpSession session = request.getSession();
		String error = null;

		// 检测用户是否登录
		Object actObject = session.getAttribute("act");
		if (actObject == null) {
			error = "没有登录！！不能显示课题信息";
			System.out.println(error);
			return;
		}

		// 如果没有三级路由，就使用默认函数
		if (pathList.size() < 3) {
			detailsProblem();
			return;
		}

		switch (pathList.get(2)) {
		case "":
			detailsProblem();
			break;
		case "teacher":
			detailsTeacher();
			break;
		case "student":
			detailsStudent();
			break;
		default:
			break;
		}
	}

	/**
	 * 显示某一课题的详情
	 * 
	 * @param problem_id
	 *            课题的ID
	 */
	public void detailsProblem() {

		jsonObjectOutput = new JSONObject();

		int problem_id = Integer.parseInt(request.getParameter("pro_id"));

		Problem problem = problemDao.queryByProblem_id(problem_id);

		if (problem == null) {
			jsonObjectOutput.put("status", false);
		} else {

			List<Object> objectList = toObjectDetails(problem);

			JSONArray objectArray = new JSONArray();

			for (Object object : objectList) {
				objectArray.add(object);
			}

			jsonObjectOutput.put("status", true);

			jsonObjectOutput.put("info", objectArray);
		}

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void detailsTeacher() {

		jsonObjectOutput = new JSONObject();

		String idString = request.getParameter("pro_id");

		int problem_id = Integer.parseInt(idString);

		Problem problem = problemDao.queryByProblem_id(problem_id);

		if (problem == null) {
			jsonObjectOutput.put("status", false);
		} else {

			List<Object> objectList = toObjectDetailsTeacher(problem);

			JSONArray objectArray = new JSONArray();

			for (Object object : objectList) {
				objectArray.add(object);
			}

			jsonObjectOutput.put("status", true);

			jsonObjectOutput.put("info", objectArray);
		}

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void detailsStudent() {

		jsonObjectOutput = new JSONObject();

		System.out.println("正在显示学生选题的题目详情！");

		String idString = request.getParameter("pro_id");

		int problem_id = Integer.parseInt(idString);

		Problem problem = problemDao.queryByProblem_id(problem_id);

		if (problem == null) {
			jsonObjectOutput.put("status", false);
		} else {

			List<Object> objectList = toObjectDetailsStudent(problem);

			JSONArray objectArray = new JSONArray();

			for (Object object : objectList) {
				objectArray.add(object);
			}

			jsonObjectOutput.put("status", true);

			jsonObjectOutput.put("info", objectArray);
		}

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 更新课题信息
	 * 
	 * @param updateList
	 *            要更新的课题数据列表
	 */
	public void updateProblem(List<String[]> updateList) {

		jsonObjectOutput = new JSONObject();
		// 待更新的课题对象列表
		List<Problem> updateProblemList = new ArrayList<>();

		// 将所有要更新的字符串数组转换成对象，放入上面的待更新对象列表中
		for (String[] strings : updateList) {
			List<Object> list = new ArrayList<>();
			for (String string : strings) {
				list.add(string);
			}
			updateProblemList.add(toBeanUpdate(list));
		}

		System.out.println("修改的课题数量为：" + updateProblemList.size());

		// 更新列表中的所有课题
		boolean flag = true;
		int problem_id = 0; // 记录出错的课题的ID
		for (int i = 0; i < updateProblemList.size(); i++) {
			System.out.println("Update Problem :"
					+ updateProblemList.get(i).toString());
			flag = problemDao.updateAll(updateProblemList.get(i));
			if (!flag) {
				problem_id = updateProblemList.get(i).getProblem_id();
				break;
			}
		}

		jsonObjectOutput.put("status", flag);

		if (!flag) {
			String info = "未知原因，ID为" + problem_id + "的课题更新失败";
			jsonObjectOutput.put("info", info);
		}

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	public void test() {

		// 在测试时使用，在Session中设置一个用户
		Teacher teacher = new TeacherDao().queryByTea_id(20015);
		HttpSession session = request.getSession();
		session.setAttribute("act", 2);
		session.setAttribute("user", teacher);

	}

	/**
	 * 导出课题的选题的信息，根据三级路由选择不同的方式导出文件
	 */
	public void export() {

		// test();

		// 没有登录就不能导出，登录是导出文件的前提。
		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "没有登录！！无法导出课题。";
			System.out.println(error);
			return;
		}
		// 如果没有三级路由，就使用默认函数
		if (pathList.size() < 3) {
			exportUser();
			return;
		}

		switch (pathList.get(2)) {
		case "":
			exportUser();
			break;
		case "majors":
			exportMajor();
			break;

		default:
			break;
		}

	}

	/**
	 * 按照用户导出课题，管理员导出所有课题的选题情况，教师或专业负责人导出自己的 所有课题的选题信息
	 */
	public void exportUser() {

		jsonObjectOutput = new JSONObject();

		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "没有登录！！无法导出课题。";
			System.out.println(error);
			return;
		}

		Integer act = Integer.parseInt(String.valueOf(actObject));
		System.out.println("act: " + act);

		List<Problem> problemList = null;
		String fileName = "选题数据";
		// 根据用户得到不同的课题
		if (act == 1) {
			// 学生登录
			error = "当前登录的用户是学生，无法导出课题信息！！";
			System.out.println(error);
			return;
		} else if (act == 3) {
			// 管理员登录，得到所有课题
			problemList = problemDao.queryAll();
			fileName = "所有选题数据";
		} else {
			// 教师或专业负责人登录，得到自己编写的所有课题
			Teacher teacher = (Teacher) session.getAttribute("user");
			fileName = teacher.getRealname() + "教师选题数据";
			System.out.println("export userinfo:" + teacher.toString());

			// 得到当前登录教师的所有课题
			problemList = problemDao.queryByTea_id(teacher.getTea_id());
		}

		// 得到要生成文件的对象
		XSSFWorkbook workbook = XLSXFile4ProblemList(problemList);

		System.out.println("开始生成数据文件。。。");

		// 返回文件数据
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(fileName, "utf-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();

			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("生成数据文件成功");

	}

	/**
	 * 根据专业导出课题的选题数据，专业负责人可以使用自己的专业导出所有课题的选题信息 管理员则根据参数，可以导出多个专业所有课题的选题信息
	 */
	public void exportMajor() {

		System.out.println("进入exportMajor方法中。。。");

		jsonObjectOutput = new JSONObject();

		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "没有登录，无法导出数据！";
			System.out.println(error);
			return;
		}

		Integer act = (Integer) actObject;

		if (act != 4) {
			error = "登录用户不是专业负责人，无法导出专业的选题数据！";
			System.out.println(error);
			return;
		}

		Teacher teacher = (Teacher) session.getAttribute("user");
		System.out.println("当前登录的用户的类型为" + (act == 4 ? "专业负责人" : "其它类型"));
		System.out.println("当前登录的用户为：" + teacher.toString());
		// 得到专业负责人负责的专业对象
		Major major = majorDao.queryByTea_id(teacher.getTea_id());

		// 得到本专业的所有课题
		List<Problem> problemList = problemDao.queryByMid(major.getMid());

		// 将对象数据生成XLSX文件数据
		XSSFWorkbook workbook = XLSXFile4ProblemList(problemList);

		System.out.println("专业选题数据 ：开始生成文件。。。");

		// 返回文件数据
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(major.getMajor() + "专业选题数据", "utf-8")
					+ ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();

			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("专业选题数据：生成数据文件成功！！");

	}

	@Deprecated
	public void exportAll() {

		jsonObjectOutput = new JSONObject();

		// 得到所有的课题数据
		List<Problem> problemList = problemDao.queryAll();

		// 将对象数据生成XLSX文件数据
		XSSFWorkbook workbook = XLSXFile4ProblemList(problemList);

		System.out.println("专业选题数据 ：开始生成文件。。。");

		// 返回文件数据
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment;filename=exportDataByTeacherID.xlsx");
			ServletOutputStream outputStream = response.getOutputStream();

			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("专业选题数据：生成数据文件成功！！");

	}

	public XSSFWorkbook XLSXFile4ProblemList(List<Problem> problemList) {
		// 生成xlsx文件
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow titleRow = sheet.createRow(0);
		// 插入标题数据
		Title2XSSFRow(titleRow);
		// 插入具体数据
		for (int i = 0; i < problemList.size(); i++) {

			XSSFRow dataRow = sheet.createRow(i + 1);
			Problem problem = problemList.get(i);
			System.out.println("生成文件之前：课题" + i + ":" + problem.toString());
			Problem2XSSFRow(workbook, problem, dataRow);
		}
		return workbook;
	}

	public void Title2XSSFRow(XSSFRow titleRow) {
		String[] titles = { "课题ID", "课题名称", "所属专业", "新课题？","课题类型", "课题来源", "课题性质",
				"选题方式", "出题时间", "所属教师姓名", "选题学生姓名", "选题时间" };
		// 插入标题信息
		for (int i = 0; i < titles.length; i++) {
			titleRow.createCell(i).setCellValue(titles[i]);
		}
	}

	public void Problem2XSSFRow(XSSFWorkbook workbook, Problem problem,
			XSSFRow dataRow) {

		dataRow.createCell(0).setCellValue(problem.getProblem_id());
		dataRow.createCell(1).setCellValue(problem.getName());
		dataRow.createCell(2).setCellValue(
				majorDao.queryByMID(problem.getMid()).getMajor());
		
		dataRow.createCell(3).setCellValue(problem.getIs_new() == 0 ? "否" : "是");
		
		// 第4列为课题类型
		int type = problem.getType();
		String typeString = "未知类型";
		switch (type) {
		case 0:
			typeString = "请选择课题类型";
			break;
		case 1:
			typeString = "毕业设计";
			break;
		case 2:
			typeString = "毕业论文";
			break;
		default:
			break;
		}
		dataRow.createCell(4).setCellValue(typeString);
		
		// 第5列为课题来源
		int source = problem.getSource();
		String sourceString = "未知来源";
		switch (source) {
		case 1:
			sourceString = "自拟题目";
			break;
		case 2:
			sourceString = "科研题目 - " + problem.getResearch_name();
			break;
		default:
			break;
		}
		dataRow.createCell(5).setCellValue(sourceString);
		
		// 第6列为课题性质
		int natrue = problem.getNature();
		String natrueString = "未知性质";
		switch (natrue) {
		case 1:
			natrueString = "理论研究";
			break;
		case 2:
			natrueString = "应用基础及其理论研究";
			break;
		case 3:
			natrueString = "工程技术研究";
			break;
		case 4:
			natrueString = "其它";
			break;
		default:
			break;
		}
		dataRow.createCell(6).setCellValue(natrueString);
		
		// 第7列为选题方式
		int way = problem.getWay();
		String wayString = "未知方式";
		switch (way) {
		case 0:
			wayString = "盲选";
			break;
		default:
			wayString = "指定学生 - " + studentDao.queryByStu_id(way).getRealname();
			break;
		}
		dataRow.createCell(7).setCellValue(wayString);
		
		// 第8列是出题日期，设置日期格式
		XSSFCell cell = dataRow.createCell(8);
		CellStyle cellStyle = workbook.createCellStyle();
		CreationHelper creationHelper = workbook.getCreationHelper();
		cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(
				"yyyy-MM-dd  hh:mm:ss"));
//		cellStyle.setAlignment(HorizontalAlignment.CENTER);  // 文本居中
		cell.setCellStyle(cellStyle);
		cell.setCellValue(new Date(problem.getProblem_time().getTime()));
		// 第9列课题所属教师的姓名
		dataRow.createCell(9).setCellValue(
				teacherDao.queryByTea_id(problem.getTea_id()).getRealname());
		// 第10列是选择这个课题的学生的姓名
		Selected selected = selectedDao.queryByProblem_id(problem
				.getProblem_id());
		dataRow.createCell(10).setCellValue(
				selected == null ? "待选" : studentDao.queryByStu_id(
						selected.getStu_id()).getRealname());
		// 第11列选题时间
		XSSFCell selectedCell = dataRow.createCell(11);
		if (selected == null) {
			selectedCell.setCellValue("待选");
		} else {
			selectedCell.setCellStyle(cellStyle);
			selectedCell.setCellValue(new Date(selected.getTime().getTime()));
		}
		
	}

	/**
	 * 审核课题，将课题状态和审核意见放入数据库中
	 */
	public void verifyProblem() {

		jsonObjectOutput = new JSONObject();

		int problem_id = Integer.parseInt(request.getParameter("pro_id"));
		String content = request.getParameter("content");
		;
		boolean status = Boolean.parseBoolean(request.getParameter("accepted"));

		Problem problem = problemDao.queryByProblem_id(problem_id);

		problem.setStatus(status ? 1 : -1);

		problem.setAudit_time(new Timestamp(new Date().getTime()));
		problem.setAudit_opinion(content);

		boolean b = problemDao.updateVerify(problem);

		jsonObjectOutput.put("status", b);

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public void selected() {

		// 没有登录就不能导出，登录是导出文件的前提。
		HttpSession session = request.getSession();
		String error = null;

		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "没有登录！！无法显示选题课题。";
			System.out.println(error);
			return;
		}

		// 如果没有三级路由，就使用默认函数
		if (pathList.size() < 3) {
			selectedProblem();
			return;
		}

		switch (pathList.get(2)) {
		case "":
			selectedProblem();
			break;
		case "majors":
			selectedMajor();
			break;
		case "student":
			selectedStudent();
			break;
		case "dels":
			selectedDels();
		default:
			break;
		}
	}

	/**
	 * 默认的查看课题的选题信息的方法，教师或专业负责人得到自己所有课题的选题信息，管理员会得到所有的课题的选题信息
	 */
	public void selectedProblem() {

		jsonObjectOutput = new JSONObject();

		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "selectedProblem: 没有登录！！无法得到课题信息。";
			System.out.println(error);
			return;
		}

		Integer act = Integer.parseInt(String.valueOf(actObject));
		System.out.println("selected act: " + act);
		
		Integer page = null;
		int pageSize = 5;
		Long count = null;
		
		List<Problem> problemList = null;

		if (act == 1) {
			// 学生登录
			// 请使用 /problem/selected/student 得到学生的选题信息
			System.out.println("学生登录，无法在本URL下得到选题信息！");
			return;
		} else if (act == 3) {
			// 管理员登录
			page = Integer.parseInt(request.getParameter("currentPage"));
			// TODO 应该是得到一页的数据
			problemList = problemDao.queryByPage(page, pageSize);
			
			count = problemDao.queryCount();
			
		} else {
			// 教师或专业负责人登录
			Teacher teacher = (Teacher) session.getAttribute("user");

			if (teacher == null) {
				error = "selectedProblem: 无法得到教师信息！！";
				System.out.println(error);
				return;
			}
			// 得到当前登录教师的所有课题
			problemList = problemDao.queryByTea_id(teacher.getTea_id());

		}

		// 判断得到的课题是否为空
		if (problemList == null) {
			error = "课题数量为空，无法得到选题信息！";
			return;
		}

		if (problemList.size() == 0) {
			error = "selectedProblem: 课题数量为0，没有信息！！";
			return;
		}

		// 将每一个课题得到是否被学生选择的信息，变成JSON数据。
		JSONArray arrayList = problemList2JSON(problemList);
		
		if (act == 3) {
			// 管理员登录
			jsonObjectOutput.put("currentPage", page);
			// 设置总页数
			jsonObjectOutput.put("totalPage", (count - 1) / pageSize + 1 );
		} 
		
		// 生成JSON数据
		jsonObjectOutput.put("data", arrayList);
		jsonObjectOutput.put("status", true);
		

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 得到一个专业的所有选题信息，专业负责人使用。
	 */
	public void selectedMajor() {

		HttpSession session = request.getSession();
		String error = null;
		Object actObject = session.getAttribute("act");

		if (actObject == null) {
			error = "selectedMajor: 没有登录！！无法得到课题信息。";
			System.out.println(error);
			return;
		}

		Integer act = Integer.parseInt(actObject.toString());
		Teacher teacher = (Teacher) session.getAttribute("user");
		// 得到课题列表
		List<Problem> problemList = null;
		if (act == 4) {
			// 专业负责人登录
			problemList = problemDao.queryByMid(teacher.getMid());
		} else if (act == 3) {
			// 管理员登录，得到专业的参数，最后得到专业对应的所有课题
			// TODO 现在得到的是所有的课题
			problemList = problemDao.queryAll();
		} else {
			error = "登录用户类型不正确，无法得到选题信息！";
			System.out.println(error);
			return;
		}

		// 课题列表转换成JSON数组
		JSONArray arrayList = problemList2JSON(problemList);

		// 生成JSON数据
		jsonObjectOutput.put("data", arrayList);
		jsonObjectOutput.put("status", true);

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 得到一个学生的选题结果信息，学生用户使用
	 */
	public void selectedStudent() {
		// 学生得到的选题信息与其它类型的用户不一样

		jsonObjectOutput = new JSONObject();

		System.out.println("学生显示选题信息中。。。");

		HttpSession session = request.getSession();
		// 没有登录
		Object actObject = session.getAttribute("act");
		String error = null;
		if (actObject == null) {
			error = "Not Login";
			System.out.println(error);
			return;
		}

		int act = Integer.parseInt(String.valueOf(actObject));

		// 不是学生登录
		if (act != 1) {
			System.out.println("Act : " + act);
			error = "Not Studnet Login";
			System.out.println(error);
			return;
		}

		Student student = (Student) session.getAttribute("user");

		// 学生选题信息
		JSONArray jsonArray = new JSONArray();		
		Selected selected = selectedDao.queryByStu_id(student.getStu_id());
		jsonObjectOutput.put("status", selected != null);
		
		if (selected != null) {
			List<Object> objectList = toObjectSelectedStudent(student);

			for (Object object : objectList) {
				jsonArray.add(object);
			}
		
			jsonObjectOutput.put("info", jsonArray);
			
			// 添加教师的额外信息
			Problem problem = problemDao
					.queryByProblem_id(selected.getProblem_id());
			Teacher teacher = teacherDao.queryByTea_id(problem.getTea_id());
			
			JSONArray teacherArray = new JSONArray();
			
			jsonObjectOutput.put("check", teacher.getShow4stu() == 1 ? true : false);
			
			if (teacher.getShow4stu() == 1) {
				
				teacherArray.add(teacher.getTitle());
				teacherArray.add(teacher.getDegree());
				teacherArray.add(teacher.getExperience());
				
				jsonObjectOutput.put("teacher", teacherArray);
			}
			
		}
		
		

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void selectedDels() {
		String[] selectedIdStrings = request.getParameterValues("ids[]");

		jsonObjectOutput = new JSONObject();

		if (selectedIdStrings.length != 0) {

			List<Integer> delList = new ArrayList<>();

			for (String string : selectedIdStrings) {
				if (string.equals("\\")) {
					break;
				}
				delList.add(Integer.parseInt(string));
			}

			List<Boolean> delFlagList = new ArrayList<>();

			for (Integer selected_id : delList) {
				boolean flag = selectedDao.remove(selected_id);
				System.out.println("delete selected id: " + selected_id
						+ ",result: " + flag);
				delFlagList.add(flag);
			}

			JSONArray delJsonArray = new JSONArray();

			for (int i = 0; i < delFlagList.size(); i++) {
				if (delFlagList.get(i)) {
					delJsonArray.add(delList.get(i));
				}

			}

			jsonObjectOutput.put("status", true);
			// 设置返回删除成功的课题的ID
			jsonObjectOutput.put("ids", delJsonArray);

			// 返回JSON数据
			try {
				response.getWriter().write(jsonObjectOutput.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public JSONArray problemList2JSON(List<Problem> problemList) {

		// 将每一个课题得到是否被学生选择的信息，变成JSON数据。
		JSONArray arrayList = new JSONArray();

		for (Problem problem : problemList) {

			JSONArray problemArray = new JSONArray();
			// 通过课题得到选题信息
			Selected selected = selectedDao.queryByProblem_id(problem
					.getProblem_id());
			// 将选题信息转换成数组
			List<Object> objectList = toObjectSelected(problem, selected);

			for (Object object : objectList) {
				problemArray.add(object);
			}

			arrayList.add(problemArray);
		}

		return arrayList;

	}

	/**
	 * 对要添加的课题的数据进行检测
	 * 
	 * @param list
	 *            要检测的数据
	 * @return 返回出错的字符串，没有错则返回空
	 */
	public String checkProblem(List<String> list) {

		String error = null;

		for (int i = 0; i < list.size() - 1; i++) {

			// 检测所有数据的非空
			if (list.get(i) == null || list.get(i).equals("")) {
				error = "数据不能为空！" + i;
				System.out.println("null index:" + i);
				break;
			}
			// 课题名称不能重复
			if (i == 0 && list.get(i) != null) {
				long number = problemDao.queryByNameCount(list.get(i));
				if (number > 0) {
					error = "课题名称重复！";
					break;
				}
			}

			// 选题方式为指定学生，检测此学生是否已经被指定过
			if (i == 6 && !list.get(i).equals("0")) {
				List<Problem> wayList = problemDao.queryByWay(Integer
						.parseInt(list.get(i)));
				if (wayList != null && wayList.size() != 0) {
					error = "该学生已经被指定课题了！";
					break;
				}
			}
		}

		return error;

	}

	/**
	 * 在添加课题时，将字符串数组转换成对象
	 * 
	 * @param list
	 *            字符串数组
	 * @return 返回一个课题对象
	 */
	public Problem toBeanAdd(List<String> list) {
		Problem problem = new Problem();

		problem.setName(list.get(0));
		problem.setMid(Integer.parseInt(list.get(1)));
		problem.setIs_new(Integer.parseInt(list.get(2)));
		problem.setType(Integer.parseInt(list.get(3)));
		problem.setSource(Integer.parseInt(list.get(4)));
		problem.setNature(Integer.parseInt(list.get(5)));
		problem.setWay(Integer.parseInt(list.get(6)));
		problem.setIntroduction(list.get(7));
		problem.setRequirement(list.get(8));
		problem.setResearch_name(list.get(9));
		return problem;

	}

	public Problem toBeanUpdate(List<Object> list) {
		Problem problem = new Problem();

		problem.setProblem_id(Integer.parseInt(String.valueOf(list.get(0))));
		problem.setName(String.valueOf(list.get(1)));
		problem.setMid(Integer.parseInt(String.valueOf(list.get(2))));
		problem.setIs_new(Integer.parseInt(String.valueOf(list.get(3))));
		problem.setType(Integer.parseInt(String.valueOf(list.get(4))));
		problem.setSource(Integer.parseInt(String.valueOf(list.get(5))));
		problem.setNature(Integer.parseInt(String.valueOf(list.get(6))));
		problem.setWay(Integer.parseInt(String.valueOf(list.get(7))));
		problem.setIntroduction(String.valueOf(list.get(8)));
		problem.setRequirement(String.valueOf(list.get(9)));
		problem.setResearch_name(String.valueOf(list.get(10)));

		return problem;
	}

	/**
	 * 显示课题时将对象转换成 Object 数组
	 * 
	 * @param problem
	 *            要转换的对象
	 * @return 返回一个 Object 列表
	 */
	public List<Object> toObjectShow(Problem problem) {
		List<Object> list = new ArrayList<>();

		list.add(problem.getProblem_id());
		list.add(problem.getName());
		list.add(majorDao.queryByMID(problem.getMid()).getMajor());
		list.add(problem.getIs_new() == 0 ? "否" : "是");
		int type = problem.getType();
		String typeString = "未知类型";
		switch (type) {
		case 0:
			typeString = "请选择课题类型";
			break;
		case 1:
			typeString = "毕业设计";
			break;
		case 2:
			typeString = "毕业论文";
			break;
		default:
			break;
		}
		list.add(typeString);

		int source = problem.getSource();
		String sourceString = "未知来源";
		switch (source) {
		case 1:
			sourceString = "自拟题目";
			break;
		case 2:
			sourceString = "科研题目 - " + problem.getResearch_name();
			break;
		default:
			break;
		}
		list.add(sourceString);

		int natrue = problem.getNature();
		String natrueString = "未知性质";
		switch (natrue) {
		case 1:
			natrueString = "理论研究";
			break;
		case 2:
			natrueString = "应用基础及其理论研究";
			break;
		case 3:
			natrueString = "工程技术研究";
			break;
		case 4:
			natrueString = "其它";
			break;
		default:
			break;
		}
		list.add(natrueString);

		int way = problem.getWay();
		String wayString = "未知方式";
		switch (way) {
		case 0:
			wayString = "盲选";
			break;
		default:
			wayString = "指定学生 - " + studentDao.queryByStu_id(way).getRealname();
			break;
		}
		list.add(wayString);

		list.add(teacherDao.queryByTea_id(problem.getTea_id()).getRealname());

		int status = problem.getStatus();
		String statusString = "未知";
		switch (status) {
		case 0:
			statusString = "未审核";
			break;
		case -1:
			statusString = "未通过审核";
			break;
		case 1:
			statusString = "通过审核";
		default:
			break;
		}
		list.add(statusString);

		return list;
	}

	public List<Object> toObjectDetailsTeacher(Problem problem) {
		List<Object> list = new ArrayList<>();

		list.add(problem.getProblem_id());
		list.add(problem.getName());
		list.add(problem.getMid());
		list.add(problem.getIs_new());
		list.add(problem.getType());

		// int source = problem.getSource();
		// String sourceString = "未知来源";
		// switch (source) {
		// case 1:
		// sourceString = "自拟题目";
		// break;
		// case 2:
		// sourceString = "科研题目 - " + problem.getResearch_name();
		// break;
		// default:
		// break;
		// }
		list.add(problem.getSource());
		list.add(problem.getResearch_name());

		// int natrue = problem.getNature();
		// String natrueString = "未知性质";
		// switch (natrue) {
		// case 1:
		// natrueString = "理论研究";
		// break;
		// case 2:
		// natrueString = "应用基础及其理论研究";
		// break;
		// case 3:
		// natrueString = "工程技术研究";
		// break;
		// case 4:
		// natrueString = "其它";
		// break;
		// default:
		// break;
		// }
		list.add(problem.getNature());

		// int way = problem.getWay();
		// String wayString = "未知方式";
		// switch (way) {
		// case 0:
		// wayString = "盲选";
		// break;
		// default:
		// wayString = "指定学生 - " + studentDao.queryByStu_id(way).getRealname();
		// break;
		// }
		list.add(problem.getWay());

		list.add(teacherDao.queryByTea_id(problem.getTea_id()).getRealname());

		// int status = problem.getStatus();
		// String statusString = "未知";
		// switch (status) {
		// case 0:
		// statusString = "未审核";
		// break;
		// case -1:
		// statusString = "未通过审核";
		// break;
		// case 1:
		// statusString = "通过审核";
		// default:
		// break;
		// }
		list.add(problem.getStatus());

		if (problem.getWay() != 0) {
			Student student = studentDao.queryByStu_id(problem.getWay());
			list.add(student.getUsername());
			list.add(student.getRealname());
		} else {
			list.add(null);
			list.add(null);
		}

		list.add(problem.getIntroduction());
		list.add(problem.getRequirement());
		list.add(problem.getAudit_opinion());

		return list;
	}

	public List<Object> toObjectDetailsStudent(Problem problem) {
		List<Object> list = new ArrayList<>();

		list.add(problem.getProblem_id());
		list.add(problem.getName());
		list.add(majorDao.queryByMID(problem.getMid()).getMajor());
		list.add(problem.getIs_new() == 0 ? "否" : "是");
		int type = problem.getType();
		String typeString = "未知类型";
		switch (type) {
		case 0:
			typeString = "请选择课题类型";
			break;
		case 1:
			typeString = "毕业设计";
			break;
		case 2:
			typeString = "毕业论文";
			break;
		default:
			break;
		}
		list.add(typeString);

		int source = problem.getSource();
		String sourceString = "未知来源";
		switch (source) {
		case 1:
			sourceString = "自拟题目";
			break;
		case 2:
			sourceString = "科研题目 - " + problem.getResearch_name();
			break;
		default:
			break;
		}
		list.add(sourceString);

		int natrue = problem.getNature();
		String natrueString = "未知性质";
		switch (natrue) {
		case 1:
			natrueString = "理论研究";
			break;
		case 2:
			natrueString = "应用基础及其理论研究";
			break;
		case 3:
			natrueString = "工程技术研究";
			break;
		case 4:
			natrueString = "其它";
			break;
		default:
			break;
		}
		list.add(natrueString);
		list.add(problem.getIntroduction());
		list.add(problem.getRequirement());

		return list;
	}

	/**
	 * 显示一个专业的课题时将对象转换成Object数组
	 * 
	 * @param problem
	 *            要转换的对象
	 * @return 一个Object 列表
	 */
	public List<Object> toObjectShowMajor4Student(Problem problem) {
		List<Object> list = new ArrayList<>();

		// TODO 学生的课题信息数据格式
		list.add(problem.getProblem_id());
		list.add(problem.getName());
		// 是否可以选择此课题
		Selected selected = selectedDao.queryByProblem_id(problem.getProblem_id());
		list.add(selected== null);
		
		return list;
	}

	/**
	 * 显示课题详情时将对象转换成 Object 数组
	 * 
	 * @param problem
	 *            要转换的对象
	 * @return 返回一个 Object 列表
	 */
	public List<Object> toObjectDetails(Problem problem) {
		List<Object> list = new ArrayList<>();

		list.add(problem.getProblem_id());
		list.add(problem.getName());
		list.add(majorDao.queryByMID(problem.getMid()).getMajor());
		list.add(problem.getIs_new() == 0 ? "否" : "是");
		int type = problem.getType();
		String typeString = "未知类型";
		switch (type) {
		case 0:
			typeString = "请选择课题类型";
			break;
		case 1:
			typeString = "毕业设计";
			break;
		case 2:
			typeString = "毕业论文";
			break;
		default:
			break;
		}
		list.add(typeString);

		int source = problem.getSource();
		String sourceString = "未知来源";
		switch (source) {
		case 1:
			sourceString = "自拟题目";
			break;
		case 2:
			sourceString = "科研题目 - " + problem.getResearch_name();
			break;
		default:
			break;
		}
		list.add(sourceString);

		int natrue = problem.getNature();
		String natrueString = "未知性质";
		switch (natrue) {
		case 1:
			natrueString = "理论研究";
			break;
		case 2:
			natrueString = "应用基础及其理论研究";
			break;
		case 3:
			natrueString = "工程技术研究";
			break;
		case 4:
			natrueString = "其它";
			break;
		default:
			break;
		}
		list.add(natrueString);

		int way = problem.getWay();
		String wayString = "未知方式";
		switch (way) {
		case 0:
			wayString = "盲选";
			break;
		default:
			wayString = "指定学生 - " + studentDao.queryByStu_id(way).getRealname();
			break;
		}
		list.add(wayString);

		list.add(teacherDao.queryByTea_id(problem.getTea_id()).getRealname());

		list.add(problem.getIntroduction());
		list.add(problem.getRequirement());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (problem.getAudit_time() != null) {
			list.add(df.format(new Date(problem.getAudit_time().getTime())));
		} else {
			list.add(null);
		}
		list.add(problem.getAudit_opinion());

		int status = problem.getStatus();
		String statusString = "未知";
		switch (status) {
		case 0:
			statusString = "未审核";
			break;
		case -1:
			statusString = "未通过审核";
			break;
		case 1:
			statusString = "通过审核";
		default:
			break;
		}
		list.add(statusString);
		// 课题教师电话
		list.add(teacherDao.queryByTea_id(problem.getTea_id()).getPhone());

		return list;
	}

	/**
	 * 显示选题信息时，将课题对象，选题对象转换成 Object 数组
	 * 
	 * @param problem
	 *            课题对象
	 * @param selected
	 *            选题对象
	 * @return 返回一个 List 数组
	 */
	public List<Object> toObjectSelected(Problem problem, Selected selected) {

		List<Object> list = new ArrayList<>();
		// 一定有的信息：专业名称，课题名称，教师帐号，教师姓名，选题方式
		// 可能 有的信息：学生学号，姓名，学生选题时间,选题ID
		Teacher teacher = teacherDao.queryByTea_id(problem.getTea_id());
		Major major = majorDao.queryByMID(problem.getMid());
		Student student = selected == null ? null : studentDao
				.queryByStu_id(selected.getStu_id());

		if (selected == null || student == null) {
			// 课题还没有人选择时
			String nullString = "\\";
			list.add(nullString);
			list.add(nullString);
			list.add(nullString);
			list.add(nullString);
		} else {
			// 选题ID
			list.add(selected.getSelected_id());
			// 学生的姓名和用户名（学号）
			list.add(student.getRealname());
			list.add(student.getUsername());
			// 添加选题学生的选题时间
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			list.add(df.format(new Date(selected.getTime().getTime())));
		}

		list.add(problem.getName());
		list.add(major.getMajor());
		list.add(teacher.getUsername());
		list.add(teacher.getRealname());

		int way = problem.getWay();
		String wayString = "未知方式";
		switch (way) {
		case 0:
			wayString = "盲选";
			break;
		default:
			wayString = "指定学生 - " + studentDao.queryByStu_id(way).getRealname();
			break;
		}
		list.add(wayString);

		return list;

	}

	/**
	 * 转换学生的选题信息
	 * 
	 * @param problem
	 * @param selected
	 * @param teacher
	 * @return
	 */
	public List<Object> toObjectSelectedStudent(Student student) {
		List<Object> list = new ArrayList<>();

		Selected selected = selectedDao.queryByStu_id(student.getStu_id());
		Problem problem = problemDao
				.queryByProblem_id(selected.getProblem_id());
		Teacher teacher = teacherDao.queryByTea_id(problem.getTea_id());

		// 课题信息
		list.add(problem.getProblem_id());
		list.add(problem.getName());
		list.add(majorDao.queryByMID(problem.getMid()).getMajor());
		list.add(problem.getIs_new() == 0 ? "否" : "是");
		int type = problem.getType();
		String typeString = "未知类型";
		switch (type) {
		case 0:
			typeString = "请选择课题类型";
			break;
		case 1:
			typeString = "毕业设计";
			break;
		case 2:
			typeString = "毕业论文";
			break;
		default:
			break;
		}
		list.add(typeString);

		int source = problem.getSource();
		String sourceString = "未知来源";
		switch (source) {
		case 1:
			sourceString = "自拟题目";
			break;
		case 2:
			sourceString = "科研题目 - " + problem.getResearch_name();
			break;
		default:
			break;
		}
		list.add(sourceString);

		int natrue = problem.getNature();
		String natrueString = "未知性质";
		switch (natrue) {
		case 1:
			natrueString = "理论研究";
			break;
		case 2:
			natrueString = "应用基础及其理论研究";
			break;
		case 3:
			natrueString = "工程技术研究";
			break;
		case 4:
			natrueString = "其它";
			break;
		default:
			break;
		}
		list.add(natrueString);

		int way = problem.getWay();
		String wayString = "未知方式";
		switch (way) {
		case 0:
			wayString = "盲选";
			break;
		default:
			wayString = "指定学生 - " + studentDao.queryByStu_id(way).getRealname();
			break;
		}
		list.add(wayString);

		list.add(problem.getIntroduction());
		list.add(problem.getRequirement());

		// 教师信息
		list.add(teacher.getRealname());
		list.add(teacher.getPhone());

		// 学生信息
		list.add(student.getUsername());
		list.add(student.getRealname());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		list.add(df.format(new Date(selected.getTime().getTime())));
		
		
		return list;

	}

	/**
	 * 得到所有的专业
	 * 
	 * @return
	 */
	public JSONArray getAllMajors() {

		List<Major> majors = majorDao.getAllMajor();
		JSONArray majorArray = new JSONArray();
		for (Major major : majors) {
			JSONArray array = new JSONArray();
			array.add(major.getMid());
			array.add(major.getMajor());
			majorArray.add(array);
		}

		return majorArray;
	}

}
