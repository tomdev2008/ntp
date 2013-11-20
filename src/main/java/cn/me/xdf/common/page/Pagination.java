package cn.me.xdf.common.page;

import java.util.List;

/**
 * 列表分页
 * 
 * @author xiaobin
 * 
 */
@SuppressWarnings("serial")
public class Pagination extends SimplePage implements java.io.Serializable,
		Paginable {

	public Pagination() {
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 */
	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	/**
	 * 构造器
	 * 
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页几条数据
	 * @param totalCount
	 *            总共几条数据
	 * @param list
	 *            分页内容
	 */
	public Pagination(int pageNo, int pageSize, int totalCount, List<?> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	/**
	 * 第一条数据位置
	 * 
	 * @return
	 */
	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	public boolean hasNextPage() {

		return ((getPageNo() + 1) * getPageSize()) < getTotalCount();
	}

	/**
	 * 当前页的数据
	 */
	private List<?> list;

	/**
	 * 获得分页内容
	 * 
	 * @return
	 */
	public List<?> getList() {
		return list;
	}

	/**
	 * 设置分页内容
	 * 
	 * @param list
	 */
	public void setList(List<?> list) {
		this.list = list;
	}
	
	/**
	 * 开始页码
	 */
	public int getStartPage() {
		int begin = 0;
		if(pageNo%10!=0){
			begin = (int) (Math.floor(pageNo/10)*10)+1;
		}else{
			begin = (pageNo/10-1)*10+1;
		}
		return begin;
	}

	/**
	 * 结束页码
	 */
	public int getEndPage() {
		int end = 0;
		if(pageNo%10!=0){
			end = (int) (Math.floor(pageNo/10)*10)+10;
			if(end>getTotalPage()){
				end = getTotalPage();
			}
		}else{
			end = pageNo;
		}
		return end;
	}
}