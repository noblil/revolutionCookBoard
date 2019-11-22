package kr.koreait.Service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.ui.Model;

import kr.koreait.dao.MvcboardDAO;

public class DeleteService implements MvcboardService {

	@Override
	public void execute(Model model) {
		System.out.println("DeleteService 클래스의 execute() 메소드 실행");
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");

//		request 객체로 넘어온 삭제할 글 번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardDAO mvcboardDAO = ctx.getBean("mvcboardDAO", MvcboardDAO.class);
		mvcboardDAO.delete(idx);
		
//		글 삭제 작업 후 돌아갈 페이지 번호를 Model 인터페이스 객체에 저장한다. => 이거 안하면 삭제하고 1페이지가 브라우저에 표시된다.
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
	}

}












