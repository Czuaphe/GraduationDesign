package com.graduation.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.graduation.dao.MajorDao;
import com.graduation.dao.ProblemDao;
import com.graduation.dao.SelectedDao;
import com.graduation.dao.StudentDao;
import com.graduation.dao.TeacherDao;
import com.graduation.entity.Major;
import com.graduation.entity.Problem;
import com.graduation.entity.Selected;
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

		jsonObjectOutput = new JSONObject();

		switch (pathList.get(1)) {
		case "add":
			String[] info = request.getParameterValues("info[]");
			addProblem(info);
			break;
		case "show":
			int currentPage = Integer.parseInt(request
					.getParameter("currentPage"));
			showProblem(currentPage);
			break;
		case "dels":
			String[] delList = request.getParameterValues("ids[]");
			deleteProblem(delList);
			break;
		case "details":
			int problem_id = Integer.parseInt(request.getParameter("pro_id"));
			detailsProblem(problem_id);
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
		case "verify":
			// 审核课题
			verifyProblem();
			break;
		case "exportByTeacher":
			exportByTeacher();
			break;
		case "exportByMajor":
			exportByMajor();
			break;
		case "exportAll":
			exportAll();
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
	 * 显示当前页的数据
	 * 
	 * @param page
	 *            页码
	 */
	public void showProblem(int page) {

		jsonObjectOutput = new JSONObject();

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

	/**
	 * 显示某一课题的详情
	 * 
	 * @param problem_id
	 *            课题的ID
	 */
	public void detailsProblem(int problem_id) {

		jsonObjectOutput = new JSONObject();

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

		// 更新列表中的所有课题
		boolean flag = true;
		int problem_id = 0; // 记录出错的课题的ID
		for (int i = 0; i < updateProblemList.size(); i++) {
			System.out.println("Update Student :"
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
	 * 按照教师ID导出所有课题
	 * 
	 */
	public void exportByTeacher() {

		// test();

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
		// 不是教师或专业负责人登录
		if (act == 1 || act == 3) {
			error = "登录身份不是教师或专业负责人，无法导出课题。";
			System.out.println(error);
			return;
		}

		Teacher teacher = (Teacher) session.getAttribute("user");

		if (teacher == null) {
			error = "无法得到教师信息，不能导出课题。";
			System.out.println(error);
			return;
		}

		// 得到当前登录教师的所有课题
		List<Problem> problemList = problemDao.queryByTea_id(teacher
				.getTea_id());

		XSSFWorkbook workbook = XLSXFile4ProblemList(problemList);

		System.out.println("开始生成数据文件。。。");

		// 返回文件数据
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment;filename=exportDataByTeacherID.xlsx");
			ServletOutputStream outputStream = response.getOutputStream();

			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("生成数据文件成功");

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

			Problem2XSSFRow(problem, dataRow);
		}
		return workbook;
	}

	public void Title2XSSFRow(XSSFRow titleRow) {
		String[] titles = { "课题ID", "课题名称", "所属专业", "课题类型", "课题来源", "课题性质",
				"选题方式", "出题时间", "所属教师姓名", "选题学生姓名" };
		// 插入标题信息
		for (int i = 0; i < titles.length; i++) {
			titleRow.createCell(i).setCellValue(titles[i]);
		}
	}

	public void Problem2XSSFRow(Problem problem, XSSFRow dataRow) {

		dataRow.createCell(0).setCellValue(problem.getProblem_id());
		dataRow.createCell(1).setCellValue(problem.getName());
		dataRow.createCell(2).setCellValue(
				majorDao.queryByMID(problem.getMid()).getMajor());
		dataRow.createCell(3).setCellValue(problem.getType());
		dataRow.createCell(4).setCellValue(problem.getSource());
		dataRow.createCell(5).setCellValue(problem.getNature());
		dataRow.createCell(6).setCellValue(problem.getWay());
		dataRow.createCell(7).setCellValue(
				new Date(problem.getProblem_time().getTime()));
		dataRow.createCell(8).setCellValue(
				teacherDao.queryByTea_id(problem.getTea_id()).getRealname());
		Selected selected = selectedDao.queryByProblem_id(problem
				.getProblem_id());
		dataRow.createCell(9).setCellValue(
				selected == null ? "无" : studentDao.queryByStu_id(
						selected.getStu_id()).getRealname());

	}

	/**
	 * 根据专业导出课题的选题数据
	 * 
	 * @param request
	 */
	public void exportByMajor() {

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

		Teacher teacher = (Teacher) request.getAttribute("user");
		// 得到专业负责人负责的专业的ID
		int mid = majorDao.queryByTea_id(teacher.getTea_id()).getMid();
		// 得到本专业的所有课题
		List<Problem> problemList = problemDao.queryByMid(mid);

		// 将对象数据生成XLSX文件数据
		XSSFWorkbook workbook = XLSXFile4ProblemList(problemList);

		System.out.println("专业选题数据 ：开始生成文件。。。");

		// 返回文件数据
		try {
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

	public void exportAll() {

		jsonObjectOutput = new JSONObject();

		// 得到所有的课题数据
		List<Problem> problemList = problemDao.queryAll();

		// 将对象数据生成XLSX文件数据
		XSSFWorkbook workbook = XLSXFile4ProblemList(problemList);

		System.out.println("专业选题数据 ：开始生成文件。。。");

		// 返回文件数据
		try {
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

	/**
	 * 审核课题，将课题状态和审核意见放入数据库中
	 */
	public void verifyProblem() {

		jsonObjectOutput = new JSONObject();

		int problem_id = Integer.parseInt(request.getParameter("pro_id"));
		String content = null;
		boolean status = Boolean.parseBoolean(request.getParameter("accepted"));
		if (!status) {
			content = request.getParameter("content");
		}

		Problem problem = problemDao.queryByProblem_id(problem_id);

		problem.setStatus(status ? 1 : 2);

		if (!status) {
			problem.setAudit_time(new Timestamp(new Date().getTime()));
			problem.setAudit_opinion(content);
		}

		boolean b = problemDao.updateAll(problem);

		jsonObjectOutput.put("status", b);

		// 返回JSON数据
		try {
			response.getWriter().write(jsonObjectOutput.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

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

		for (int i = 0; i < list.size(); i++) {

			// 检测所有数据的非空
			if (list.get(i) == null || list.get(i).equals("")) {
				error = "数据不能为空！";
				break;
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
		problem.setResearch_name(String.valueOf(list.get(6)));
		problem.setNature(Integer.parseInt(String.valueOf(list.get(7))));
		problem.setWay(Integer.parseInt(String.valueOf(list.get(8))));
		problem.setIntroduction(String.valueOf(list.get(9)));
		problem.setRequirement(String.valueOf(list.get(10)));

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
		list.add(problem.getMid());
		list.add(problem.getIs_new());
		list.add(problem.getType());
		list.add(problem.getSource());
		list.add(problem.getNature());
		list.add(problem.getWay());
		list.add(problem.getStatus());

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
		list.add(problem.getMid());
		list.add(problem.getIs_new());
		list.add(problem.getType());
		list.add(problem.getSource());
		list.add(problem.getResearch_name());
		list.add(problem.getNature());
		list.add(problem.getWay());
		list.add(problem.getIntroduction());
		list.add(problem.getRequirement());

		// 添加指定学生的姓名
		int way = problem.getWay();
		if (way == 0) {
			list.add(null);
		} else {
			list.add(studentDao.queryByStu_id(way).getRealname());
		}

		list.add(problem.getAudit_opinion());

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
