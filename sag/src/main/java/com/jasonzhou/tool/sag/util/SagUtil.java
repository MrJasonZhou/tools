package com.jasonzhou.tool.sag.util;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * コンバートUtil
 * 
 * @author 周
 *
 */
public class SagUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SagUtil.class);
	
	public static final String DEFAULE_DATE_FMT = "yyyyMMdd";

	/**
	 * ビーンの属性値を取得する（存在しない場合、インスタンスを生成してビーンに設定する）
	 * 
	 * @param bean			ビーンオブジェクト
	 * @param property		属性名（複数指定不可）
	 * @return	ビーンの属性値
	 * @throws Exception 
	 */
	public static Object getElseCreate(Object bean, String property) throws Exception {
		Object obj = get(bean, property);
		if (obj == null) {
			Class<?> cls = PropertyUtils.getPropertyType(bean, property);
			obj = cls.getDeclaredConstructor().newInstance();
			PropertyUtils.setProperty(bean, property, obj);
		}
		return obj;
	}

	
	/**
	 * ビーンの属性値を取得する
	 * 
	 * @param bean			ビーンオブジェクト
	 * @param properties	属性名（カンマ区切り複数可）
	 * @return	ビーンの属性値（複数の場合、リスト）
	 * @throws Exception 
	 */
	public static Object get(Object bean, String properties) throws Exception {
		if (bean == null) {
			return null;
		}
		//属性名＝"."の場合、bean自身を返す
		if (StringUtils.equals(properties, ".")|| StringUtils.isBlank(properties)) {
			return bean;
		}
		String[] arr = SagUtil.split(properties, ",");
		if (arr.length == 0) {
			return null;
		}
		if (arr.length == 1) {
			Object result = getSingle(bean, properties);
			return result;
		}
		List<Object> list = new ArrayList<>();
		for (String property : arr) {
			list.add(getSingle(bean, property));
		}
		return list;
	}
	
	/**
	 * 単一属性を取得する
	 * 
	 * @param bean	ビーン
	 * @param property	属性名（ネスト可能）
	 * @return	属性値
	 * @throws Exception
	 */
	private static Object getSingle(Object bean, String property) throws Exception {
		if (StringUtils.contains(property, "[")) {
			String[] arr = SagUtil.split(property, "[");
			Object value = PropertyUtils.getNestedProperty(bean, arr[0]);
			if (value == null) {
				logger.warn("属性取得できません。bean=" + bean.getClass().getName() + " ,属性＝" + property);
				return null;
			}
			if (value.getClass().isArray()) {
				Object[] objs = (Object[])value;
				int index = Integer.parseInt(SagUtil.split(arr[1], "]")[0]);
				if (objs== null ||  index >= objs.length) {
					logger.warn("属性取得できません。bean=" + bean.getClass().getName() + " ,属性＝" + property);
					return null;
				}
				return objs[index];
			}
			if (value instanceof List) {
				List list = (List) value;
				int index = Integer.parseInt(SagUtil.split(arr[1], "]")[0]);
				if (list == null || index >= list.size()) {
					logger.warn("属性取得できません。bean=" + bean.getClass().getName() + " ,属性＝" + property);
					return null;
				}
				return list.get(index);
			}
			
 		}
		Object result = PropertyUtils.getNestedProperty(bean, property);
		if (result == null) {
			logger.warn("属性取得できません。bean=" + bean.getClass().getName() + " ,属性＝" + property);
		}
		return result;
	}
	
	/**
	 * 単一属性を設定する
	 * 
	 * @param bean	ビーン
	 * @param property	属性名（ネスト可能）
	 * @param value 属性値
	 * @throws Exception
	 */
	private static void setSingle(Object bean, String property, Object value) throws Exception {
		try {
			PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(bean, property);
			Object castValue = cast(value, pd.getPropertyType());
			PropertyUtils.setNestedProperty(bean, property, castValue);
		} catch (Exception e) {
			logger.warn("値設定時エラーが発生しました。bean=" + bean + ", 属性＝" + property + ", value=" + value, e);
			throw e;
		}
	}
	
	
	private static Object cast(Object value, Class<?> cls) throws Exception {
		if (value == null) {
			return null;
		}
		if (cls.isAssignableFrom(value.getClass())) {
			return value;
		}
		Object result = null;
		if (value instanceof String) {
			String s = (String) value;
			switch(cls.getName()) {
			case "java.lang.Integer":
				result = Integer.parseInt(s);
				break;
			case "java.lang.Long":
				result = Long.parseLong(s);
				break;
			case "java.math.BigDecimal":
				result = new BigDecimal(s);
				break;
			case "java.util.Date":
				result = parseDate(s);
				break;
			default :
				logger.error("キャストエラー：" + value.getClass() + "->" + cls + ":" + value);
				throw new Exception("キャストエラー");
			}
		}
		if (value instanceof Integer) {
			Integer i = (Integer) value;
			switch(cls.getName()) {
			case "java.lang.String":
				result = String.valueOf(i);
				break;
			case "java.math.BigDecimal":
				result = new BigDecimal(i);
				break;
			default :
				logger.error("キャストエラー：" + value.getClass() + "->" + cls + ":" + value);
				throw new Exception("キャストエラー");
			}
		}
		if (value instanceof Long) {
			Long l = (Long) value;
			switch(cls.getName()) {
			case "java.lang.String":
				result = String.valueOf(l);
				break;
			case "java.math.BigDecimal":
				result = new BigDecimal(l);
				break;
			default :
				logger.error("キャストエラー：" + value.getClass() + "->" + cls + ":" + value);
				throw new Exception("キャストエラー");
			}
		}
		if (value instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) value;
			switch(cls.getName()) {
			case "java.lang.String":
				result = bd.toPlainString();
				break;
			case "java.lang.Integer":
				result = bd.intValue();
				break;
			case "java.lang.Long":
				result = bd.longValue();
				break;
			default :
				logger.error("キャストエラー：" + value.getClass() + "->" + cls + ":" + value);
				throw new Exception("キャストエラー");
			}
		}
		if (value instanceof Date) {
			Date date = (Date) value;
			switch(cls.getName()) {
			case "java.lang.String":
				result = fmt(date);
				break;
			case "java.lang.Long":
				result = date.getTime();
				break;
			default :
				logger.error("キャストエラー：" + value.getClass() + "->" + cls + ":" + value);
				throw new Exception("キャストエラー");
			}
		}
		if (value instanceof Timestamp) {
			Timestamp bd = (Timestamp) value;
			Calendar cal = Calendar.getInstance();
			switch(cls.getName()) {
			case "java.util.Date":
				cal.setTimeInMillis(bd.getTime());
				result = cal.getTime();
				break;
			case "java.lang.String":
				cal.setTimeInMillis(bd.getTime());
				result = fmt(cal.getTime());
				break;
			case "java.math.Long":
				result = bd.getTime();
				break;
			default :
				logger.error("キャストエラー：" + value.getClass() + "->" + cls + ":" + value);
				throw new Exception("キャストエラー");
			}
		}
		if (result == null) {
			logger.error("キャストエラー：" + value.getClass() + "->" + cls + ":" + value);
			throw new Exception("キャストエラー");
		}
		return result;
	}

	private static String fmt(Date date) {
		DateFormat df = new SimpleDateFormat(DEFAULE_DATE_FMT);
		return df.format(date);
	}
	
	public static Date parseDate(String text) throws ParseException {
		ParseException pe = new ParseException(text, 0);
		
		String[] fmts = new String[] {
				"yyyyMMdd"
				, "yyyy/MM/dd"
				, "yyyy-MM-dd"
				,"yyyyMMddHHmmss"
				, "yyyy/MM/dd HH:mm:ss"
				, "yyyy-MM-dd HH:mm:ss"
				, "yyyy-MM-dd'T'HH:mm:ssZ"};
		for (String fmt : fmts) {
			if (StringUtils.length(text) == StringUtils.length(fmt)) {
				SimpleDateFormat sdf = new SimpleDateFormat(fmt);
				try {
					return sdf.parse(text);
				} catch(ParseException e) {
					pe = e;
				}
			}
		}
		throw pe;
	}
	
	public static Date parseDate(String text, String fmt) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.parse(text);
	}
	
	public static String formatDate(Date date, String fmt) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}
	/**
	 * ビーンの属性値を設定する
	 * 
	 * @param bean			ビーンオブジェクト
	 * @param properties	属性名（カンマ区切り複数可）
	 * @param value			属性値
	 * @return	ビーンの属性値（複数の場合、リスト）
	 * @throws Exception 
	 */
	public static void set(Object bean, String properties, Object value) throws Exception {
		if (bean == null) {
			return ;
		}
		String[] arr = SagUtil.split(properties, ",");
		if (arr.length == 0) {
			return ;
		}
		if (arr.length == 1) {
			setSingle(bean, properties, value);
			return;
		}
		List<Object> list = new ArrayList<>();
		List<Object> valueList = new ArrayList<>();
		//属性値は配列の場合
		if (value.getClass().isArray()) {
			Object[] objArr = (Object[]) value;
			for (Object obj : objArr) {
				valueList.add(obj);
			}
		}
		//属性値はCollectionの場合
		if (value instanceof Collection) {
			Collection<Object> objCol = (Collection<Object>) value;
			for (Object obj : objCol) {
				valueList.add(obj);
			}
		}
		//サイズ合わなく
		if (valueList.size() != arr.length) {
			logger.warn("属性値を設定時値とサイズ不一致：属性名＝" + properties + ", 値レングス＝" + valueList.size());
			return ;
		}
		//値設定する
		for (int i = 0; i < arr.length; i++) {
			setSingle(bean, arr[i], valueList.get(i));
		}
	}
	
	/**
	 * 文字列からマッピング情報を生成する
	 * 
	 * @param mappingText	key1=value1,key2=value2の形式の文字列
	 * @return	マッピング情報
	 */
	public static Map<String, Object> toMap(String mappingText) {
		Map<String, Object> map = new HashMap<>();
		String[] exprArr = SagUtil.split(mappingText, ",");
		for (String expr : exprArr) {
			String[] arr = SagUtil.split(expr, "=", 2);
			if (arr.length >= 2) {
				map.put(StringUtils.trim(arr[0]), StringUtils.trim(arr[1]));
			}
		}
		return map;
	}
	
	/**
	 * オブジェクトを文字列に変換する
	 * 
	 * @param value	オブジェクト
	 * @return	文字列形式
	 */
	public static String asString(Object value) {
		if (value == null) {
			return "";
		}
		String text = value.toString();
		if (value instanceof BigDecimal) {
			text = ((BigDecimal) value).toPlainString();
		}
		return text;
	}
	
	/**
	 * 複数であるかを判断する
	 * 
	 * @param value　判断対象
	 * @return true：配列　又は　リストの形式
	 */
	public static boolean isMult(Object value) {
		if (value == null) {
			return false;
		}
		if (value.getClass().isArray()) {
			return true;
		}
		if (value instanceof List) {
			return true;
		}
		return false;
	}
	
	/**
	 * 配列　または　リスト形式の値をList形式に統一する
	 * 
	 * @param value	対象値
	 * @return	リスト
	 */
	public static List<Object> toList(Object value) {
		if (!isMult(value)) {
			return null;
		}
		List<Object> result = new ArrayList<>();
		if (value.getClass().isArray()) {
			Object[] objs = (Object[]) value;
			for (Object obj : objs) {
				result.add(obj);
			}
		} else {
			if (value instanceof List) {
				List list = (List) value;
				for (Object obj : list) {
					result.add(obj);
				}
			}
		}
		return result;
	}

	/**
	 * 配列　または　リスト形式の値を文字列List形式に統一する
	 * 
	 * @param value	対象値
	 * @return	文字列リスト
	 */
	public static List<String> toStringList(Object input) {
		if (!isMult(input)) {
			return null;
		}
		List<String> result = new ArrayList<>();
		List<Object> objList = toList(input);
		for (Object obj : objList) {
			result.add(asString(obj));
		}
		return result;
	}
	
    /**
     * 文字列を分割する
     * 
     * @param str	文字列
     * @param separatorChars	区切り文字列
     * @return	分割された文字列の配列
     */
    public static String[] split(final String str, final String separatorChars) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(str, separatorChars);
    }
    
    /**
     * 文字列を分割する
     * 
     * @param str	文字列
     * @param separatorChars	区切り文字列
     * @param max 最大分割数
     * @return	分割された文字列の配列
     */
    public static String[] split(final String str, final String separatorChars, int max) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(str, separatorChars, max);
    }
    
    //全角＜ー＞半角変換
    private static final String[][] KANA_NORMAL_MAP = new String[][] {
    	// [0] 半角
    	{
    		"ｱ", "ｲ", "ｳ", "ｴ", "ｵ", "ｶ", "ｷ", "ｸ", "ｹ", "ｺ",
    		"ｻ", "ｼ", "ｽ", "ｾ", "ｿ", "ﾀ", "ﾁ", "ﾂ", "ﾃ", "ﾄ",
    		"ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ", "ﾊ", "ﾋ", "ﾌ", "ﾍ", "ﾎ",
    		"ﾏ", "ﾐ", "ﾑ", "ﾒ", "ﾓ", "ﾔ", "ﾕ", "ﾖ",
    		"ﾗ", "ﾘ", "ﾙ", "ﾚ", "ﾛ", "ﾜ", "ｦ", "ﾝ"
    	},
    	// [1] 全角
    	{
    		"ア", "イ", "ウ", "エ", "オ", "カ", "キ", "ク", "ケ", "コ",
    		"サ", "シ", "ス", "セ", "ソ", "タ", "チ", "ツ", "テ", "ト",
    		"ナ", "ニ", "ヌ", "ネ", "ノ", "ハ", "ヒ", "フ", "ヘ", "ホ",
    		"マ", "ミ", "ム", "メ", "モ", "ヤ", "ユ", "ヨ",
    		"ラ", "リ", "ル", "レ", "ロ", "ワ", "ヲ", "ン"
    	}
    };
    private static final String[][] KANA_COMBINED_MAP = new String[][] {
    	// [0] 半角
    	{
    		// 濁点付き
    		"ｳﾞ", 
    		"ｶﾞ", "ｷﾞ", "ｸﾞ", "ｹﾞ", "ｺﾞ", "ｻﾞ", "ｼﾞ", "ｽﾞ", "ｾﾞ", "ｿﾞ",
    		"ﾀﾞ", "ﾁﾞ", "ﾂﾞ", "ﾃﾞ", "ﾄﾞ", "ﾊﾞ", "ﾋﾞ", "ﾌﾞ", "ﾍﾞ", "ﾎﾞ",
    		// 半濁点付き
    		"ﾊﾟ", "ﾋﾟ", "ﾌﾟ", "ﾍﾟ", "ﾎﾟ"
    	},
    	// [1] 全角
    	{
    		// 濁音
    		"ヴ", 
    		"ガ", "ギ", "グ", "ゲ", "ゴ", "ザ", "ジ", "ズ", "ゼ", "ゾ",
    		"ダ", "ヂ", "ヅ", "デ", "ド", "バ", "ビ", "ブ", "ベ", "ボ",
    		// 半濁音
    		"パ", "ピ", "プ", "ペ", "ポ"
    	}
    };
    private static final String[][] KANA_SMAL_MAP = new String[][] {
    	// [0] 半角
    	{
    		"ｧ", "ｨ", "ｩ", "ｪ", "ｫ", "ｬ", "ｭ", "ｮ", "ｯ"
    	},
    	// [1] 全角
    	{
    		"ァ", "ィ", "ゥ", "ェ", "ォ", "ャ", "ュ", "ョ", "ッ"
    	}
    };
    private static final String[][] KANA_SYMBOL_MAP = new String[][] {
    	// [0] 半角
    	{
    		// 長音、句読点
    		"ｰ", "｡", "､",
    		// 濁点
    		"ﾞ",
    		// 半濁点
    		"ﾟ"
    	},
    	// [1] 全角
    	{
    		// 長音、句読点
    		"ー", "。", "、",
    		// 濁点 (U+309B KATAKANA-HIRAGANA VOICED SOUND MARK)
    		"゛",
    		// 半濁点 (U+309C KATAKANA-HIRAGANA SEMI-VOICED SOUND MARK)
    		"゜"
    	}
    };
    private static final String[][] SPACE_SMAL_MAP = new String[][] {
    	// [0] 半角
    	{
    		" "
    	},
    	// [1] 全角
    	{
    		"　"
    	}
    };
    private static final String[][][] SIMPLE_MAPSET = new String[][][] {
    	/* ①通常. */
    	KANA_NORMAL_MAP,
    	/* ②濁音/半濁音. */
    	KANA_COMBINED_MAP,
    	/* ③小文字. */
    	KANA_SMAL_MAP,
    	/* ④記号. */
    	KANA_SYMBOL_MAP,
    	/* ⑤スペース. */
    	SPACE_SMAL_MAP
    };
    /* [0] 半角. */
    private static final int MAP_INDEX_HALF = 0;
    /* [1] 全角. */
    private static final int MAP_INDEX_FULL = 1;

    /**
     * 全角カナ文字列→半角カナ文字列
     * 
     * @param text	全角カナ文字列	
     * @return	半角カナ文字列
     */
    public static String toHalfKana(String text) {
    	final int iSrc, iTgt;
    	String ret = text;
		// [1] 全角 => [0] 半角
		iSrc = MAP_INDEX_FULL;
		iTgt = MAP_INDEX_HALF;
    	
    	for(String map[][]: SIMPLE_MAPSET) {
    		final int max = Math.min(map[0].length, map[1].length);
    		for(int i = 0; i < max; i++) {
    			final String src = map[iSrc][i];
    			final String tgt = map[iTgt][i];
    			ret = ret.replace(src, tgt);
    		}
    	}
    	return ret;
    }

    /**
     * 半角カナ文字列→全角カナ文字列
     * 
     * @param text	半角カナ文字列	
     * @return	全角カナ文字列
     */
    public static String toFullKana(String text) {
    	final int iSrc, iTgt;
    	String ret = text;
		// [0] 半角 => [1] 全角
		iSrc = MAP_INDEX_HALF;
		iTgt = MAP_INDEX_FULL;
    	
    	for(String map[][]: SIMPLE_MAPSET) {
    		final int max = Math.min(map[0].length, map[1].length);
    		for(int i = 0; i < max; i++) {
    			final String src = map[iSrc][i];
    			final String tgt = map[iTgt][i];
    			ret = ret.replace(src, tgt);
    		}
    	}
    	return ret;
    }
    
    /**
     * 文字列を連結する
     * 
     * @param list	文字列のリスト
     * @return	連結された文字列
     */
    public static String join(List<String> list) {
    	return join(list, "");
    }

    /**
     * 文字列を連結する
     * 
     * @param list	文字列のリスト
     * @param del	区切り文字列
     * @return	連結された文字列
     */
    public static String join(List<String> list, String del) {
    	String[] arr = new String[list.size()] ;
    	for (int i = 0; i < list.size(); i++) {
    		arr[i] = list.get(i);
    	}
    	return StringUtils.join(arr,del );
    }

}
