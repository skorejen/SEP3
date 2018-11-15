package controller.requests;

import controller.Controller;
import model.Book;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController implements ApplicationContextAware {

    private ConfigurableApplicationContext context;

    @RequestMapping(method=RequestMethod.POST, value="/book")
    public String addBook(@RequestBody Book book)
    {
    	System.out.println(book.toString());
        Controller controller = context.getBean(Controller.class);
       
       return controller.addBook(book);
      
    }
    
    @RequestMapping(method=RequestMethod.DELETE, value="/book")
    public String deleteBook(@RequestBody String isbn) {
    	
    	Controller controller = context.getBean(Controller.class);
    	return controller.deleteBook(isbn); 
    	
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = (ConfigurableApplicationContext) applicationContext;
    }
    
    
}
