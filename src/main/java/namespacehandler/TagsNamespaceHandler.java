package namespacehandler;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Node;

/**
 * Created by zhu on 2019/4/1.
 */
public class TagsNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {

    }

    @Override
    public BeanDefinitionHolder decorate(Node node, BeanDefinitionHolder definition, ParserContext parserContext) {
        return super.decorate(node, definition, parserContext);
    }
}
