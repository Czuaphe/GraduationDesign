package com.graduation.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.graduation.dao.AdminDao;
import com.graduation.dao.NoticeDao;
import com.graduation.entity.Notice;
import com.graduation.entity.Problem;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NoticeService {

	private NoticeDao noticeDao = new NoticeDao();

	private AdminDao adminDao = new AdminDao();

	private List<String> pathList = new ArrayList<>();

	private HttpServletRequest request = null;

	private JSONObject jsonObjectOutput = new JSONObject();

	public NoticeService() {
	}

	public NoticeService(List<String> pathList, HttpServletRequest request) {
		this.pathList = pathList;
		this.request = request;
	}

	public JSONObject redirectToPath() {

		jsonObjectOutput = new JSONObject();
		HttpSession session = request.getSession();
		String error = null;
		
		// 检测用户是否登录
		Object actObject = session.getAttribute("act");
		if (actObject == null) {
			error = "没有登录！！不能显示课题信息";
			System.out.println(error);
			return null;
		}

		switch (pathList.get(1)) {
		case "add":
			addNotice();
			break;
		case "update":
			updateNotice();
			break;
		case "dels":
			deleteNotice();
			break;
		case "show":
			showNotice();
			break;
		default:
			break;
		}

		return jsonObjectOutput;

	}

	/**
	 * 添加公告
	 */
	public void addNotice() {

		jsonObjectOutput = new JSONObject();

		HttpSession session = request.getSession();
		String errorString = null;

		// 检测用户是否登录
		Object actObject = session.getAttribute("act");
		if (actObject == null) {
			errorString = "没有登录！！不能显示课题信息";
			System.out.println(errorString);
			return;
		}

		int act = Integer.parseInt(String.valueOf(actObject));

		if (act != 3) {
			errorString = "除管理员外，其它用户不能登录";
			System.out.println(errorString);
			return;
		}

		System.out.println("正在添加公告中。。。");

		String[] infoArray = request.getParameterValues("info[]");

		List<String> list = new ArrayList<>();
		for (String string : infoArray) {
			System.out.println(string);
			list.add(string);
		}

		Map<Integer, String> errorMap = checkNotice(list);
		System.out.println("error size:" + errorMap.size());
		if (errorMap.size() > 0) {

			JSONArray errorArray = new JSONArray();
			
			Set<Integer> set = errorMap.keySet();
			
			for (Integer integer : set) {
				JSONArray array = new JSONArray();
				array.add(integer);
				array.add(errorMap.get(integer));
				errorArray.add(array);
			}

			jsonObjectOutput.put("status", false);
			jsonObjectOutput.put("errors", errorArray);
			return;
		}

		Notice notice = null;
		try {
			notice = toBeanAdd(list);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		boolean b = noticeDao.save(notice);

		jsonObjectOutput.put("status", b);
		if (!b) {
			jsonObjectOutput.put("error", "未知原因保存失败");
		}
	}

	public void updateNotice() {

		jsonObjectOutput = new JSONObject();

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

		// 待更新的公告对象列表
		List<Notice> updateNoticeList = new ArrayList<>();

		// 将所有要更新的字符串数组转换成对象，放入上面的待更新对象列表中
		for (String[] strings : updateStringList) {
			List<String> list = new ArrayList<>();
			for (String string : strings) {
				list.add(string);
			}
			
			try {
				updateNoticeList.add(toBeanUpdate(list));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		System.out.println("修改的公告数量为：" + updateNoticeList.size());

		// 更新列表中的所有课题
		boolean flag = true;
		int notice_id = 0; // 记录出错的公告的ID
		for (int i = 0; i < updateNoticeList.size(); i++) {
			System.out.println("Update Notice :"
					+ updateNoticeList.get(i).toString());
			flag = noticeDao.updateAll(updateNoticeList.get(i));
			if (!flag) {
				notice_id = updateNoticeList.get(i).getNotice_id();
				break;
			}
		}

		jsonObjectOutput.put("status", flag);

		if (!flag) {
			String info = "未知原因，ID为" + notice_id + "的公告更新失败";
			jsonObjectOutput.put("info", info);
		}

	}

	public void deleteNotice() {

		jsonObjectOutput = new JSONObject();
		// 得到要删除的公告ID数组
		String[] list = request.getParameterValues("ids[]");

		if (list.length != 0) {

			List<Integer> delList = new ArrayList<>();

			for (String string : list) {
				delList.add(Integer.parseInt(string));
			}

			List<Boolean> delFlagList = new ArrayList<>();

			for (Integer notice_id : delList) {
				boolean flag = noticeDao.remove(notice_id);
				System.out.println("delete id: " + notice_id + ",result: "
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
			// 设置返回删除成功的公告的ID
			jsonObjectOutput.put("ids", delJsonArray);
		}
	}

	public void showNotice() {

		jsonObjectOutput = new JSONObject();

		// 得到要显示的页数
		int page = Integer.parseInt(request.getParameter("currentPage"));

		boolean flag = true;

		int pageSize = 5;
		// 得到当前页的所有对象
		List<Notice> list = noticeDao.queryByPage(page, pageSize);
		
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		
		if (list != null) {
			for (Notice notice : list) {
				if (notice != null) {
					flag = false;
					break;
				}
			}
		}

		// 课题的总数量
		long count = noticeDao.queryCount();

		JSONArray jsonList = new JSONArray();
		// 将对象转换成 JSON 数据
		for (Notice notice : list) {

			List<Object> objectList = toObjectShow(notice);

			jsonList.add(objectList);
		}
		// 返回所有专业
		// jsonObjectOutput.put("majors", getAllMajors());

		// 设置要返回的课题数据
		jsonObjectOutput.put("data", jsonList);
		// 设置当前页
		jsonObjectOutput.put("currentPage", page);
		// 设置返回状态
		jsonObjectOutput.put("status", flag);
		// 设置总页数
		jsonObjectOutput.put("totalPage", (count - 1) / pageSize + 1);

	}

	public Map<Integer, String> checkNotice(List<String> list) {

		Map<Integer, String> map = new HashMap<Integer, String>();
		
		String NOT_NULL = "不能为空！";
		String END_ERROR = "结束时间不能早于开始时间";
		
		for (int i = 0; i < list.size(); i++) {
			
			String string = list.get(i);
			if (i == 0) {
				if (string == null || string.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			if (i == 1) {
				if (string == null || string.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
			if (i == 2) {
				if (string == null || string.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
				String start = list.get(1);
				if (start== null || start.compareTo(string) > 0) {
					map.put(i, END_ERROR);
				}
			}
			if (i == 3) {
				if (string == null || string.equals("")) {
					map.put(i, NOT_NULL);
					continue;
				}
			}
		}
		
		return map;

	}

	public Notice toBeanAdd(List<String> list) throws ParseException {

		Notice notice = new Notice();

		notice.setContent(list.get(0));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse(list.get(1));
		System.out.println("公告的开始日期为：" + start.toString());
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		notice.setStart(new Timestamp(start.getTime()));
		Date end = df.parse(list.get(2));
		System.out.println("公告的结束日期为：" + end.toString());
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		notice.setEnd(new Timestamp(end.getTime()));

		int canSee = Integer.parseInt(list.get(3));

		switch (canSee) {
		case 1:
			notice.setP1(1);
			notice.setP2(0);
			notice.setP4(0);
			break;
		case 2:
			notice.setP1(0);
			notice.setP2(1);
			notice.setP4(0);
			break;
		case 3:
			notice.setP1(0);
			notice.setP2(0);
			notice.setP4(1);
			break;
		case 4:
			notice.setP1(1);
			notice.setP2(1);
			notice.setP4(1);
			break;
		default:
			break;
		}
		return notice;

	}

	public Notice toBeanUpdate(List<String> list) throws ParseException {

		Notice notice = new Notice();
		notice.setNotice_id(Integer.parseInt(list.get(0)));
		notice.setContent(list.get(1));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date start = df.parse(list.get(2));
		System.out.println("公告的开始日期为：" + start.toString());
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		notice.setStart(new Timestamp(start.getTime()));
		Date end = df.parse(list.get(3));
		System.out.println("公告的结束日期为：" + end.toString());
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		notice.setEnd(new Timestamp(end.getTime()));

		int canSee = Integer.parseInt(list.get(4));

		switch (canSee) {
		case 1:
			notice.setP1(1);
			notice.setP2(0);
			notice.setP4(0);
			break;
		case 2:
			notice.setP1(0);
			notice.setP2(1);
			notice.setP4(0);
			break;
		case 3:
			notice.setP1(0);
			notice.setP2(0);
			notice.setP4(1);
			break;
		case 4:
			notice.setP1(1);
			notice.setP2(1);
			notice.setP4(1);
			break;
		default:
			break;
		}
		
		return notice;
	}

	public List<Object> toObjectShow(Notice notice) {

		List<Object> list = new ArrayList<>();
		list.add(notice.getNotice_id());
		list.add(notice.getContent());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		list.add(df.format(notice.getStart()));
		list.add(df.format(notice.getEnd()));
		
		int p1 = notice.getP1();
		int p2 = notice.getP2();
		int p4 = notice.getP4();
		
		if (p1 == 1 && p2 == 0 && p4 == 0) {
			list.add(1);
		}
		if (p1 == 0 && p2 == 1 && p4 == 0) {
			list.add(2);
		}
		if (p1 == 0 && p2 == 0 && p4 == 1) {
			list.add(3);
		}
		if (p1 == 1 && p2 == 1 && p4 == 1) {
			list.add(4);
		}
		
		return list;
	}

}
