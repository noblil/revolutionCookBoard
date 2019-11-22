package kr.koreait.Service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.ui.Model;

import kr.koreait.dao.MvcboardDAO;
import kr.koreait.vo.MvcboardVO;

public class UpdateService implements MvcboardService {

	@Override
	public void execute(Model model) {
		System.out.println("UpdateService 클래스의 execute() 메소드 실행");
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");

//		request 객체로 넘어온 수정할 글 번호와 수정할 데이터를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardDAO mvcboardDAO = ctx.getBean("mvcboardDAO", MvcboardDAO.class);
		
//		글을 수정하는 메소드를 실행한다.
//		mvcboardDAO.update(idx, subject, content);
		MvcboardVO mvcboardVO = ctx.getBean("mvcboardVO", MvcboardVO.class);
		mvcboardVO.setIdx(idx);
		mvcboardVO.setSubject(subject);
		mvcboardVO.setContent(content);
		mvcboardDAO.update(mvcboardVO);

//		글 수정 작업 후 돌아갈 페이지 번호를 Model 인터페이스 객체에 저장한다. => 이거 안하면 수정하고 1페이지가 브라우저에 표시된다.
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
	}

}











