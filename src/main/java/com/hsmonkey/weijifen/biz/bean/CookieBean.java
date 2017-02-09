package com.hsmonkey.weijifen.biz.bean;

import com.hsmonkey.weijifen.util.StringsUtil;

import wint.lang.utils.StringUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年1月12日  下午2:08:41</p>
 * <p>作者：niepeng</p>
 */
public class CookieBean {

	// _m_h5_tk 30f9b9f5ad0b6417b411916f7fe2be25_1452486703777
	private String _m_h5_tk;
	
	// _m_h5_tk_enc 9072bda9cfad6149bcaa5a19520cf137
	private String _m_h5_tk_enc;
	

	// -------------- extend attribute --------------------

	// -------------- normal method -----------------------
	
	public long getTime() {
		if (StringUtil.isBlank(_m_h5_tk)) {
			return 0L;
		}
		String[] tmp = _m_h5_tk.split("_");
		if (tmp.length != 2) {
			return 0L;
		}

		return StringsUtil.str2long(tmp[1]);
	}
	
	public String getCookieForSignPrefix() {
		String[] tmp = _m_h5_tk.split("_");
		return tmp[0];
	}
	
	public boolean checkDataIsRight() {
		if (StringUtil.isBlank(_m_h5_tk) || StringUtil.isBlank(_m_h5_tk_enc)) {
			return false;
		}

		String[] tmp = _m_h5_tk.split("_");
		if (tmp.length != 2) {
			return false;
		}

		long time = StringsUtil.str2long(tmp[1]);
		if (time < System.currentTimeMillis()) {
			return false;
		}

		return true;
	}

	// -------------- setter/getter -----------------------


	public String get_m_h5_tk() {
		return _m_h5_tk;
	}

	public void set_m_h5_tk(String _m_h5_tk) {
		this._m_h5_tk = _m_h5_tk;
	}

	public String get_m_h5_tk_enc() {
		return _m_h5_tk_enc;
	}

	public void set_m_h5_tk_enc(String _m_h5_tk_enc) {
		this._m_h5_tk_enc = _m_h5_tk_enc;
	}
	
}
