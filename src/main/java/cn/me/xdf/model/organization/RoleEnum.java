package cn.me.xdf.model.organization;/** * 角色枚举类 * <p/> * * <pre> * 定义了针对新东方备课平台的6种角色 * 1:管理员:对所有模块可见 * 2:集团主管：创建基础资源 * 3：学校主管：发起备课流程 * 4：批课教师: * 5:指导教师：对新教师提交的资料可见 * 6：新教师：备课闯关 * 7:默认：没有权限 * </pre> * * @author xiaobin */public enum RoleEnum {	admin {		@Override		public String getValue() {			return "系统管理员";		}		@Override		public String getKey() {			return "admin";		}		@Override		public String getUrl() {			return "/dashboard/admin-dashboard";		}		@Override		public int getIndex() {			return 6;		}	},	group {		@Override		public String getValue() {			return "主管";		}		@Override		public String getKey() {			return "group";		}		@Override		public String getUrl() {			return "/dashboard/group-dashboard";		}		@Override		public int getIndex() {			return 5;		}	},	campus {		@Override		public String getValue() {			return "学校主管";		}		@Override		public String getKey() {			return "campus";		}		@Override		public String getUrl() {			return "/dashboard/campus-dashboard";		}		@Override		public int getIndex() {			return 4;		}	},	appofclass { // 线下		@Override		public String getValue() {			return "批课教师";		}		@Override		public String getKey() {			return "appofclass";		}		@Override		public String getUrl() {			return null;		}		@Override		public int getIndex() {			return 3;		}	},	guidance {		@Override		public String getValue() {			return "导师";		}		@Override		public String getKey() {			return "guidance";		}		@Override		public String getUrl() {			return "/dashboard/coach-dashboard";		}		@Override		public int getIndex() {			return 2;		}	},	trainee {		@Override		public String getValue() {			return "新教师";		}		@Override		public String getKey() {			return "trainee";		}		@Override		public String getUrl() {			return "/dashboard/trainee-dashboard";		}		@Override		public int getIndex() {			return 1;		}	},	default_role {		@Override		public String getValue() {			return "默认角色";		}		@Override		public String getKey() {			return null;		}		@Override		public String getUrl() {			return "/login";		}		@Override		public int getIndex() {			return 0;		}	};	public abstract String getValue();	public abstract String getKey();	public abstract String getUrl();	public abstract int getIndex();}