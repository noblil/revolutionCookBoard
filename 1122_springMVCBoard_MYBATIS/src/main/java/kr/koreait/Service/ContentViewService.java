package kr.koreait.Service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.ui.Model;

import kr.koreait.dao.MvcboardDAO;
import kr.koreait.vo.MvcboardVO;

public class ContentViewService implements MvcboardService {

	@Override
	public void execute(Model model) {
		System.out.println("ContentViewService 클래스의 execute() 메소드 실행");
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
//		request 객체로 넘어온 조회수를 증가시킨(브라우저에 출력할) 글 번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardDAO mvcboardDAO = ctx.getBean("mvcboardDAO", MvcboardDAO.class);
		
//		조회수를 증가시킨 글 한 건을 얻어와서 MvcboardVO 클래스 객체에 저장한다.
		MvcboardVO mvcboardVO = ctx.getBean("mvcboardVO", MvcboardVO.class);
		mvcboardVO = mvcboardDAO.selectByIdx(idx);
//		System.out.println(mvcboardVO);
		
//		브라우저에 출력할 글, 작업 후 돌아갈 페이지 번호, 줄바꿈 구현에 사용할 "\r\n"을 Model 인터페이스 객체에 저장한다.
		model.addAttribute("vo", mvcboardVO);
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
		model.addAttribute("rn", "\r\n");
	}

}






