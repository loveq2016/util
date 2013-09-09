package com.util.permissions;

public final class PermissionsUtil {

	/** 
     * 四种权限 ，当前定义为int，以下二进制表示只取后四位作说明 
     */  
	public static final int create = 1;  // 添加  
    
	public static final int read = 2;  // 查询   
     
	public static final int update = 4; // 修改
    
	public static final int delete = 8; // 删除   
	
	public static final int audit = 16;// 审核   
	
	public static final int comment = 32; //评论
	
	
	public static boolean isCreate(int authorityCode) {
		return ((authorityCode & create) == create);
	}
	
	public static boolean isRead(int authorityCode) {
		return ((authorityCode & read) == read);
	}
	
	public static boolean isUpdate(int authorityCode) {
		return ((authorityCode & update) == update);
	}
	
	public static boolean isDelete(int authorityCode) {
		return ((authorityCode & delete) == delete);
	}
	
	public static boolean isAudit(int authorityCode) {
		return ((authorityCode & audit) == audit);
	}
	
	public static boolean isComment(int authorityCode) {
		return ((authorityCode & comment) == comment);
	}
	public static int cancelRead(int authorityCode) {
		return authorityCode & (~read);
	}
	
	
	public static int cancelCreate(int authorityCode) {
		return authorityCode & (~create);
	}
	
	public static int cancelUpdate(int authorityCode) {
		return authorityCode & (~update);
	}
	
	public static int cancelDelete(int authorityCode) {
		return authorityCode & (~delete);
	}
	
	public static int cancelAudit(int authorityCode) {
		return authorityCode & (~audit);
	}
	
	public static int cancelComment(int authorityCode) {
		return authorityCode & (~comment);
	}
	
	public static int appendCreate(int authorityCode) {
		return authorityCode | create;
	}
	
	public static int appendRead(int authorityCode) {
		return authorityCode | read;
	}
	
	public static int appendUpdate(int authorityCode) {
		return authorityCode | update;
	}
	
	public static int appendDelete(int authorityCode) {
		return authorityCode | delete;
	}
	
	public static int appendAudit(int authorityCode) {
		return authorityCode | audit;
	}
	
	public static int appendComment(int authorityCode) {
		return authorityCode | comment;
	}
	
	public static void main(String[] args) {
		
	 
		System.out.println(appendCreate(read));
		
		System.out.println(appendDelete(update));
		/*int authorityCode = 15 | 16;
		
		System.out.println(isAudit(authorityCode));
		
		System.out.println(isCreate(authorityCode));
		
		System.out.println(isUpdate(authorityCode));
		
		System.out.println(isDelete(authorityCode));
		
		System.out.println(isRead(authorityCode));
		
		System.out.println(isComment(authorityCode));*/
		/*authorityCode = cancelAudit(authorityCode);
		
		System.out.println(isAudit(authorityCode));*/
	}
}
