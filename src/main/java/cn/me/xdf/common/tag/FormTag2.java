package cn.me.xdf.common.tag;

import jodd.servlet.HtmlEncoder;
import jodd.servlet.HtmlFormUtil;
import jodd.servlet.HtmlTag;
import jodd.servlet.JspResolver;
import jodd.util.StringUtil;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

@SuppressWarnings("serial")
public class FormTag2 extends BodyTagSupport {

	private static final String INPUT = "input";
	private static final String TYPE = "type";
	private static final String VALUE = "value";
	private static final String TEXT = "text";
	private static final String SELECT = "select";
	private static final String HIDDEN = "hidden";
	private static final String IMAGE = "image";
	private static final String PASSWORD = "password";
	private static final String CHECKBOX = "checkbox";
	private static final String TRUE = "true";
	private static final String CHECKED = "checked";
	private static final String RADIO = "radio";
	private static final String TEXTAREA = "textarea";
	private static final String NAME = "name";
	private static final String OPTION = "option";
	private static final String SELECTED = "selected";
	private static final String IDREF = "@id(";

	private String beanName = null;
	private boolean isAutoName = true;

	public void setBean(String v) {
		beanName = v;
	}

	public String getBean() {
		return beanName;
	}

	public boolean isAutoName() {
		return isAutoName;
	}

	public void setAutoName(boolean isAutoName) {
		this.isAutoName = isAutoName;
	}

	// ----------------------------------------------------------------
	// interface

	/**
	 * Resolve form fields.
	 */
	public interface FieldResolver {
		/**
		 * Resolves form field value.
		 */
		Object value(String name);
	}

	// ---------------------------------------------------------------- tag

	/**
	 * Starts the tag.
	 */
	@Override
	public int doStartTag() {
		return EVAL_BODY_AGAIN;
	}

	/**
	 * Performs smart form population.
	 */
	@Override
	public int doAfterBody() throws JspException {
		BodyContent body = getBodyContent();
		JspWriter out = body.getEnclosingWriter();

		String bodytext = populateForm(body.getString(), new FieldResolver() {
			public Object value(String name) {
				if (isAutoName)
					return JspResolver.value(
							String.format("%s.%s", beanName, name), pageContext);
				else
					return JspResolver.value(name, pageContext);
			}
		});

		try {
			out.print(bodytext);
		} catch (IOException ioex) {
			throw new JspException(ioex);
		}
		return SKIP_BODY;
	}

	/**
	 * Ends the tag.
	 */
	@Override
	public int doEndTag() {
		return EVAL_PAGE;
	}

	// ---------------------------------------------------------------- populate

	/**
	 * Populates HTML form.
	 */
	protected String populateForm(String html, FieldResolver resolver) {
		int s = 0;
		StringBuilder result = new StringBuilder((int) (html.length() * 1.2));
		String currentSelectName = null;
		HtmlTag tag = null;
		while (true) {
			if (tag != null) {
				result.append(tag);
			}
			tag = HtmlTag.locateNextTag(html, s);
			if (tag == null) {
				result.append(html.substring(s));
				break;
			}
			result.append(html.substring(s, tag.getStartIndex()));
			s = tag.getNextIndex();

			String tagName = tag.getTagName();
			// process end tags
			if (tag.isEndTag()) {
				if (tagName.equals(SELECT)) {
					currentSelectName = null;
				}
				continue;
			}

			if (tagName.equals(INPUT) == true) {
				// INPUT
				String tagType = tag.getAttribute(TYPE);
				if (tagType == null) {
					continue;
				}
				String name = tag.getAttribute(NAME);
				if (name == null) {
					continue;
				}
				Object valueObject = resolver.value(name);
				if (valueObject == null) {
					continue;
				}
				String value = valueObject.toString();
				tagType = tagType.toLowerCase();

				if (tagType.equals(TEXT)) {
					if (tag.hasAttribute(VALUE)) {
						continue;
					}
					tag.setAttribute(VALUE, value);
				} else if (tagType.equals(HIDDEN)) {
					if (tag.hasAttribute(VALUE)) {
						continue;
					}
					tag.setAttribute(VALUE, value);
				} else if (tagType.equals(IMAGE)) {
					if (tag.hasAttribute(VALUE)) {
						continue;
					}
					tag.setAttribute(VALUE, value);
				} else if (tagType.equals(PASSWORD)) {
					tag.setAttribute(VALUE, value);
				} else if (tagType.equals(CHECKBOX)) {
					if (tag.hasAttribute(CHECKED)) {
						continue;
					}
					String tagValue = tag.getAttribute(VALUE);
					if (tagValue == null) {
						tagValue = TRUE;
					}
					if (valueObject.getClass().isArray()) {
						// checkbox group
						String vs[] = StringUtil.toStringArray(valueObject);
						for (String vsk : vs) {
							if ((vsk != null) && (vsk.equals(tagValue))) {
								tag.setAttribute(CHECKED);
							}
						}
					} else if (tagValue.equals(value)) {
						tag.setAttribute(CHECKED);
					}
				} else if (tagType.equals(RADIO)) {
					if (tag.hasAttribute(CHECKED)) {
						continue;
					}
					String tagValue = tag.getAttribute(VALUE);
					if (tagValue != null) {
						if (tagValue.equals(value)) {
							tag.setAttribute(CHECKED);
						}
					}
				}
			} else if (tagName.equals(TEXTAREA)) {
				String name = tag.getAttribute(NAME);
				//String value = tag.getAttribute("value");
				Object valueObject = resolver.value(name);
				if (valueObject != null) {
					tag.setSuffixText(HtmlEncoder.text(valueObject
							.toString()));
				}
			} else if (tagName.equals(SELECT)) {
				currentSelectName = tag.getAttribute(NAME);
			} else if (tagName.equals(OPTION)) {
				if (currentSelectName == null) {
					continue;
				}
				if (tag.hasAttribute(SELECTED)) {
					continue;
				}
				String tagValue = tag.getAttribute(VALUE);
				if (tagValue != null) {
					Object vals = resolver.value(currentSelectName);
					if (vals == null) {
						continue;
					}
					if (vals.getClass().isArray()) {
						String vs[] = StringUtil.toStringArray(vals);
						for (String vsk : vs) {
							if ((vsk != null) && (vsk.equals(tagValue))) {
								tag.setAttribute(SELECTED);
							}
						}
					} else {
						String value = StringUtil.toString(vals);
						if (value.equals(tagValue)) {
							tag.setAttribute(SELECTED);
						}
					}
				}
			}
		}
		return result.toString();
	}

	/**
	 * Replaces the reference if found, otherwise returns <code>null</code>.
	 */
	protected static String replaceReference(String formId, String value) {
		int ndx = value.indexOf(IDREF);
		if (ndx == -1) {
			return null;
		}
		int ndx2 = value.indexOf(')', ndx);
		if (ndx2 == -1) {
			throw new IllegalArgumentException("ID reference not closed: "
					+ value);
		}
		String ref = value.substring(ndx + IDREF.length(), ndx2);
		return value.substring(0, ndx) + formId + HtmlFormUtil.name2id(ref)
				+ value.substring(ndx2 + 1);
	}
}
