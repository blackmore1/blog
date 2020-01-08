package com.demon4u.blog.util;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class FreemarkerUtil {
    private final static BeansWrapper wrapper = BeansWrapper
            .getDefaultInstance();
    private final static TemplateHashModel staticModels = wrapper
            .getStaticModels();

    /**
     * ftl中调用类的静态方法或属性
     * */
    public static TemplateHashModel useStaticPacker(String packname) {
        TemplateHashModel fileStatics = null;
        try {
            fileStatics = (TemplateHashModel) staticModels.get(packname);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        return fileStatics;
    };
}
