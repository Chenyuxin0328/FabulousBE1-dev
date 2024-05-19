package com.creatorsn.fabulous.util;

public class RegexPattern {

    final public static String Email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    final public static String GUID = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    final public static String Phone = "^1[3456789]\\d{9}$";
    final public static String BASE64Image = "^data:image/(jpg|jpeg|png|gif);base64,.*$";
    final public static String VerifiedCode = "^\\d{6}$";
    final public static String DirectoryName = "^[a-zA-Z0-9_\\-\\u4e00-\\u9fa5 ]+$";
    public static final String PATH = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}(/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})*$";
    public static final String PathWithName = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}(/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})*/[\\.a-zA-Z0-9_\\-\\u4e00-\\u9fa5 ]+";
    public static final String GUIDList = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}(,^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12})*$";
    public static final String ItemSortKey = "^(id|name|createDate|updateDate|metadata\\.title|metadata\\.publisher|metadata\\.year)$";

    public static final String GUIDPDF = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}(\\.pdf){0,1}$";
}
