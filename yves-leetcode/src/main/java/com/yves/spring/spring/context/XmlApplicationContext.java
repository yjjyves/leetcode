package com.yves.spring.spring.context;

import com.yves.spring.beans.BeanDefinitionRegistry;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-11:52
 */
public class XmlApplicationContext extends AbstractApplicationContext {

    private List<Resource> resources;

    private XmlBeanDefinitionReader reader;

    public XmlApplicationContext(String... location) throws Throwable {
        super();
        load(location);
        this.reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) this.beanFactory);
        reader.loadBeanDefintions((Resource[]) resources.toArray());
    }

    @Override
    public Resource getResource(String location) throws IOException {
        if (StringUtils.isNotBlank(location)) {
            if (location.startsWith(Resource.CLASS_PATH_PREFIX)) {
                return new ClassPathResource(location.substring(Resource.CLASS_PATH_PREFIX.length()));
            } else if (location.startsWith(Resource.FILE_PATH_PREFIX)) {
                return new FileSystemResource(location.substring(Resource.FILE_PATH_PREFIX.length()));
            } else {
                return new UrlResource(location);
            }
        }
        return null;
    }


    private void load(String[] location) throws IOException {
        if (resources == null) {
            resources = new ArrayList<>();
        }

        if (location != null && location.length > 0) {
            for (String loc : location) {
                Resource resource = getResource(loc);
                if (resource != null) {
                    resources.add(resource);
                }
            }
        }
    }


}
