package com.scsb.db;

import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;

public class DBResultSet{
	Vector rows = null;
	String[] title = null;
	public static String TITLE = "title";
	public static String ROW = "row";
	static Logger logger = Logger.getLogger(DBResultSet.class.getName());
	
	/**
	 * 查詢結果的建構子
	 */
	public DBResultSet(ResultSet rs) throws SQLException {
		super();

		if (rs == null)
			return;

		ResultSetMetaData aMetaData = rs.getMetaData();
		int fields = aMetaData.getColumnCount();
		title = new String[fields];
		for (int i = 0; i < fields; i++)
			title[i] = aMetaData.getColumnName(i + 1);

		rs.beforeFirst();
		rows = new Vector();
		while (rs.next()) {
			Object[] columns = new Object[title.length];
			for (int i = 0; i < title.length; i++) {
				if (rs.getObject(title[i]) == null)
					columns[i] = "";
				else {
					Object object = rs.getObject(title[i]);
					if (object instanceof oracle.sql.CLOB) {
						oracle.sql.CLOB clob = (oracle.sql.CLOB) object;
						Reader reader = clob.getCharacterStream();
						if (reader == null)
							columns[i] = "";
						else {
							StringBuffer sb = new StringBuffer();
							try {
								int c = -1;
								while ((c = reader.read()) != -1)
									sb.append((char) c);
								columns[i] = sb.toString();
							} catch (Exception e) {
								logger.error(e.toString());
								columns[i] = "";
							}
						}
					} else {
						if (object instanceof byte[])	//java.sql.Types : BINARY,VARBINARY,LONGVARBINARY,BLOB
							columns[i] = new String(rs.getBytes(title[i]));
						else
							columns[i] = rs.getObject(title[i]).toString().trim();
					}
				}
			}
			rows.add(columns);
		}
	}

	/**
	 * 查詢結果的建構子(為了網路傳輸而創的建構子)
	 */
	public DBResultSet(HashMap hashMap) {
		super();

		this.title = (String[]) hashMap.get(TITLE);
		this.rows = (Vector) hashMap.get(ROW);
	}

	/**
	 * 查詢結果的建構子(為了網路傳輸而創的建構子)
	 */
	public DBResultSet(String[] title, Vector rows) {
		super();

		this.title = title;
		this.rows = rows;
	}

	/**
	 * 抓取第幾個欄位的名稱
	 */
	public String getTitle(int index) {
		if (title != null && index < title.length)
			return title[index];
		else
			return null;
	}

	/**
	 * 找出輸入的欄位名稱是第幾個Column
	 */
	public int getTitleIndex(String key) {
		int result = -1;

		for (int i = 0; i < title.length; i++)
			if (title[i].equalsIgnoreCase(key))
				result = i;

		return result;
	}

	/**
	 * 知道第幾個Column後,找出value值位於哪一個row
	 */
	public int getRowIndexByColumn(int column, String value) {
		int result = -1;

		Vector ColumnData = getVectorWithSomeColumn(column);
		if (ColumnData != null && ColumnData.size() > 0)
			for (int i = 0; i < ColumnData.size() && result < 0; i++)
				if (getPositionData(i, column).equalsIgnoreCase(value))
					result = i;

		return result;
	}

	/**
	 * 總共幾筆資料
	 */
	public int getSize() {
		if (rows == null)
			return 0;
		return rows.size();
	}

	/**
	 * 總共有幾個欄位
	 */
	public int getColumnSize() {
		return title.length;
	}

	/**
	 * 抓取第幾筆的第幾個欄位資料
	 */
	public String getPositionData(int x, int y) {
		if (rows == null || rows.size() == 0)
			return null;

		if (rows.size() <= x || title.length <= y)
			return null;

		return ((Object[]) rows.get(x))[y].toString();
	}

	/**
	 * 抓取第幾筆資料
	 */
	public Object[] getRowData(int index) {
		if (index < getSize())
			return (Object[]) rows.get(index);
		else
			return null;
	}

	public Vector getRows() {
		return rows;
	}

	public String[] getTitle() {
		return title;
	}

	public HashMap getTransferHashMap() {
		HashMap result = new HashMap();

		result.put(TITLE, getTitle());
		result.put(ROW, getRows());

		return result;
	}

	/**
	 * 將每一列的第幾欄撈出來
	 */
	public Vector getVectorWithSomeColumn(int column) {
		if (column > getColumnSize())
			return null;

		Vector result = new Vector();
		for (int i = 0; i < getSize(); i++)
			result.add(getPositionData(i, column));

		return result;
	}

	/**
	 * 用[欄位名稱]排序後產生新的查詢物件回傳(升冪排列:order=0,降冪排列:order=1)
	 */
	public DBResultSet getOrderSetBy(String FieldName, int order) {
		DBResultSet result = null;
		int column = getTitleIndex(FieldName);

		if (getRows() != null && getSize() > 0 && column < getSize()) {
			// 先排序出key值(PS:重複的key值會被省略)
			TreeSet sortKeys = new TreeSet();
			for (int i = 0; i < getSize(); i++)
				sortKeys.add(getPositionData(i, column));

			// 依序將這些key值row data過濾出來
			Object[] keys = sortKeys.toArray();
			Vector NewData = new Vector();
			if (order == 0) {// 升冪
				for (int i = 0; i < keys.length; i++)
					for (int j = 0; j < getSize(); j++)
						if (getPositionData(j, column).equals(keys[i]))
							NewData.add(getRowData(j));
			} else if (order == 1) {// 降冪
				for (int i = keys.length - 1; i >= 0; i--)
					for (int j = 0; j < getSize(); j++)
						if (getPositionData(j, column).equals(keys[i]))
							NewData.add(getRowData(j));
			}

			HashMap hm = new HashMap();
			hm.put(TITLE, getTitle());
			hm.put(ROW, NewData);
			result = new DBResultSet(hm);
		}

		return result;
	}
}