package Control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import UI.TabbedPane;

public class StaffControl {
	POS mPos;
	String mName;
	String mPosition;
	String mSales;
	Connection db;
	public String mCurrentErrorMessage = "";
	static int mStaffId = 1000;

	public StaffControl(POS pos) {
		mPos = pos;
		db = mPos.jdbc.db;
	}

	public int searchStaff(String searchName) {

		int res = -1;
		try {
			String sqlStr = "select * from staff where name = '" + searchName + "'";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			mName = rs.getString("name");
			mPosition = rs.getString("position");
			mSales = rs.getString("sales");
			res = 1;

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			mCurrentErrorMessage = "검색 결과 없음.";
		}
		return res;
	}

	public void searchAndShowStaff(String searchName) {
		int res = searchStaff(searchName);
		if (res == 1) {
			String result = "";
			result += "직원명 : " + mName + "\n";
			result += "직    급 : " + mPosition + "\n";
			result += "총실적 : " + mSales + "\n";
			TabbedPane.setStaffResultArea(result);
		} else {
			TabbedPane.setStaffResultArea(mCurrentErrorMessage);
		}
	}

	public int addStaff(String name, String position) {
		int res = -1;
		
		res = validCheck(name);
		
		if(res == -1)	return res;
		
		try {
			res = -1;
			String sqlStr = "insert into staff (id, name, position) values(" + (getNextStaffId()) + ", '" + name
					+ "','" + position + "')";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.close();
			stmt.close();
			res = 1;

		} catch (SQLException e) {
			mCurrentErrorMessage = "다시 입력하세요.";
		}
		return res;
	}
	public int addAndCommitStaff(String name, String position){
		int res = -1;
		res = addStaff(name, position);
		if(res == -1) return res;
		
		try{
			db.commit();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	public int validCheck(String name){
		int res = -1;
		if (searchStaff(name) == 1) {
			res = -1;
			mCurrentErrorMessage = "이미 존재하는 이름입니다.";
			return res;
		}

		int strLength = POS.GetStringLength(name);
		if (strLength > 20) {
			mCurrentErrorMessage = "글자 수 제한";
			return res;
		}
		res = 1;
		return res;
	}


	public int getNextStaffId() {
		int nextId = mStaffId;
		try{
			String sqlStr = "select count(name) from staff";
			PreparedStatement stmt = db.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int count = rs.getInt("count(name)");
			nextId = mStaffId + count;
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return nextId;
	}
}
