package kr.koreait.springMVCBoard_DBCP;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.koreait.Service.ContentViewService;
import kr.koreait.Service.DeleteService;
import kr.koreait.Service.IncrementService;
import kr.koreait.Service.InsertService;
import kr.koreait.Service.MvcboardService;
import kr.koreait.Service.ReplyService;
import kr.koreait.Service.SelectService;
import kr.koreait.Service.UpdateService;
import kr.koreait.dao.MvcboardDAO;
import kr.koreait.dao.MybatisDAO;
import kr.koreait.vo.MvcboardList;
import kr.koreait.vo.MvcboardVO;

@Controller
public class HomeController {
	
//	여기 부터
//	private JdbcTemplate jdbcTemplate;
//	
//	public JdbcTemplate getJdbcTemplate() {
//		return jdbcTemplate;
//	}
//	@Autowired
//	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
//		this.jdbcTemplate = jdbcTemplate;
//		Constant.template = this.jdbcTemplate;
//	}
//	여기 까지 DBCP Template에서 사용하는 부분이므로 mybatis로 코드 변환이 완료되면 주석으로 처리한다.

//	servlet-context.xml 파일에서 생성한 mybatis bean을 사용하기 위해 SqlSession 인터페이스 객체를 선언한다.
//	servlet-context.xml 파일에서 생성한 mybatis bean을 자동으로 읽어와 SqlSession 인터페이스 객체에 넣어주도록 하기 위해 @Autowired 어노테이션을
//	붙여준다.
//	DBCP Template을 사용할 때는 setter 메소드에 @Autowired 어노테이션을 붙여서 초기화 했었지만 mybatis는 스프링이 알아서 초기화 시킨다.
	@Autowired
	public SqlSession sqlSession;
	
	@RequestMapping("/")
	public String index(HttpServletRequest request, Model model) {
		return "index";
	}
	
//	게시글 입력 폼을 실행하는 메소드
	@RequestMapping("/insert")
	public String insert(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 insert() 메소드 실행");
		return "insert";
	}
	
//	입력 폼에 입력된 데이터를 테이블에 저장하고 브라우저에 출력할 한 페이지 분량의 글을 얻어오는 컨트롤러의 메소드를 호출하는 메소드
	@RequestMapping("/insertOK")
	public String insertOK(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 insertOK() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("insert", InsertService.class);
//		service.execute(model);
		
//		insert.jsp에서 HttpServletRequest 인터페이스 객체로 넘어온 데이터를 받는다.
		String name = request.getParameter("name");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
//		VO 클래스 객체에 받은 데이터를 넣어준다.
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardVO mvcboardVO = ctx.getBean("mvcboardVO", MvcboardVO.class);
		mvcboardVO.setName(name);
		mvcboardVO.setSubject(subject);
		mvcboardVO.setContent(content);
		
//		mapper를 얻어온다. => MybatisDAO 인터페이스를 사용한다.
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		sql 명령을 실행한다.
//		xml 파일에서 param1, param2, param3, ... 방식으로 받아야 한다.
//		mapper.insert(name, subject, content);
//		xml 파일에서 데이터를 클래스의 멤버 이름으로 받을 수 있다. => JSP에서 사용하던 방법
		mapper.insert(mvcboardVO);
		
		return "redirect:list";
	}
	
//	브라우저에 출력할 한 페이지 분량의 글을 얻어오고 한 페이지 분량의 글을 브라우저에 출력하는 페이지를 호출하는 메소드
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 list() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("select", SelectService.class);
//		service.execute(model);
		
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		브라우저 화면에 출력할 글의 개수를 정한다.
		int pageSize = 10;
//		컨트롤러에서 HttpServletRequest 인터페이스 객체에 저장되서 넘어온 화면에 표시할 페이지 번호를 받는다.
		int currentPage = 1;
		try {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		} catch(Exception e) { }
//		테이블에 저장된 전체 글의 개수를 얻어온다.
		int totalCount = mapper.selectCount();
//		System.out.println(totalCount);
		
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		한 페이지 분량의 글과 페이지 작업에 사용할 8개의 변수를 기억하는 MvcboardList 클래스의 bean을 얻어온다.
		MvcboardList mvcboardList = ctx.getBean("mvcboardList", MvcboardList.class);
//		페이지 작업에 사용할 8개의 변수를 초기화 시키는 메소드를 실행한다.
		mvcboardList.initMvcboardList(pageSize, totalCount, currentPage);

//		MvcboardList 클래스의 한 페이지 분량의 글을 기억하는 ArrayList에 한 페이지 분량의 글을 테이블에서 얻어와 넣어준다.
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		hmap.put("startNo", mvcboardList.getStartNo());
		hmap.put("endNo", mvcboardList.getEndNo());
		mvcboardList.setMvcboardList(mapper.selectList(hmap));
//		System.out.println(mvcboardList);
		
//		list.jsp로 넘겨줄 데이터를 Model 인터페이스 객체에 넣어준다.
		model.addAttribute("mvcboardList", mvcboardList);
		
		return "list";
	}
	
//	조회수를 증가시키는 메소드
	@RequestMapping("/increment")
	public String increment(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 increment() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("increment", IncrementService.class);
//		service.execute(model);
		
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		HttpServletRequest 객체로 넘어온 조회수를 증가시킬 글 번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
//		조회수를 증가시키는 메소드를 실행한다.
		mapper.increment(idx);
		
//		조회수를 증가시킨 후 브라우저에 표시할 글 번호와 작업 후 돌아갈 페이지 번호를 Model 인터페이스 객체에 저장한다.
		model.addAttribute("idx", idx);
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
		
		return "redirect:contentView";
	}
	
//	조회수를 증가시킨 글 한 건을 브라우저에 출력하기 위해 얻어오는 메소드
	@RequestMapping("/contentView")
	public String contentView(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 contentView() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("contentView", ContentViewService.class);
//		service.execute(model);
		
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		HttpServletRequest 인터페이스 객체로 넘어온 조회수를 증가시킨(브라우저에 출력할) 글 번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
//		조회수를 증가시킨 글 한 건을 얻어와서 MvcboardVO 클래스 객체에 저장한다.
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardVO mvcboardVO = ctx.getBean("mvcboardVO", MvcboardVO.class);
		mvcboardVO = mapper.selectByIdx(idx);
//		System.out.println(mvcboardVO);
		
//		브라우저에 출력할 글, 작업 후 돌아갈 페이지 번호, 줄바꿈 구현에 사용할 "\r\n"을 Model 인터페이스 객체에 저장한다.
		model.addAttribute("vo", mvcboardVO);
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
		model.addAttribute("rn", "\r\n");
		
		return "contentView";
	}
	
//	글 한 건을 삭제하는 메소드
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 delete() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("delete", DeleteService.class);
//		service.execute(model);
		
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		HttpServletRequest 인터페이스 객체로 넘어온 삭제할 글 번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
//		글 한 건을 삭제하는 메소드를 실행한다.
		mapper.delete(idx);
		
//		글 삭제 작업 후 돌아갈 페이지 번호를 Model 인터페이스 객체에 저장한다. => 이거 안하면 삭제하고 1페이지가 브라우저에 표시된다.
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
		
		return "redirect:list";
	}
	
//	글 한 건을 수정하는 메소드
	@RequestMapping("/update")
	public String update(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 update() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("update", UpdateService.class);
//		service.execute(model);
		
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		HttpServletRequest 인터페이스 객체로 넘어온 수정할 글 번호와 수정할 데이터를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
//		글 한 건을 수정하는 메소드를 실행한다.
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardVO mvcboardVO = ctx.getBean("mvcboardVO", MvcboardVO.class);
		mvcboardVO.setIdx(idx);
		mvcboardVO.setSubject(subject);
		mvcboardVO.setContent(content);
		mapper.update(mvcboardVO);

//		글 수정 작업 후 돌아갈 페이지 번호를 Model 인터페이스 객체에 저장한다. => 이거 안하면 수정하고 1페이지가 브라우저에 표시된다.
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
	
		return "redirect:list";
	}
	
//	답글을 입력하기 위해서 브라우저 화면에 출력할 메인글을 얻어오고 답글을 입력하는 페이지를 호출하는 메소드
	@RequestMapping("/reply")
	public String reply(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 reply() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("contentView", ContentViewService.class);
//		service.execute(model);
		
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		HttpServletRequest 인터페이스 객체로 넘어온 질문글의 글 번호를 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
//		질문글 한 건을 얻어와서 MvcboardVO 클래스 객체에 저장한다.
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardVO mvcboardVO = ctx.getBean("mvcboardVO", MvcboardVO.class);
		mvcboardVO = mapper.selectByIdx(idx);
//		System.out.println(mvcboardVO);
		
//		브라우저에 출력할 글, 작업 후 돌아갈 페이지 번호, 줄바꿈 구현에 사용할 "\r\n"을 Model 인터페이스 객체에 저장한다.
		model.addAttribute("vo", mvcboardVO);
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
		model.addAttribute("rn", "\r\n");
		
		return "reply";
	}
	
//	답글을 위치에 맞게 저장하는 메소드
	@RequestMapping("/replyInsert")
	public String replyInsert(HttpServletRequest request, Model model) {
		System.out.println("컨트롤러의 replyInsert() 메소드 실행");
//		model.addAttribute("request", request);
//		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
//		MvcboardService service = ctx.getBean("reply", ReplyService.class);
//		service.execute(model);
		
		MybatisDAO mapper = sqlSession.getMapper(MybatisDAO.class);
		
//		HttpServletRequest 인터페이스 객체로 넘어온 답변할 원본 글의 글 번호, 글 그룹, 글 레벨, 같은 그룹에서 글 출력 순서, 답글 작성자 이름, 
//		답글 제목, 답글 내용을 받는다.
		int idx = Integer.parseInt(request.getParameter("idx"));
		int ref = Integer.parseInt(request.getParameter("ref"));
		int lev = Integer.parseInt(request.getParameter("lev"));
		int seq = Integer.parseInt(request.getParameter("seq"));
		String name = request.getParameter("name");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");

//		답글 데이터를 MvcboardVO 클래스 객체에 저장한다. => 답글은 질문글 바로 아래에 위치해야 하므로 lev와 seq는 1씩 증가시켜 저장한다.
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		MvcboardVO mvcboardVO = ctx.getBean("mvcboardVO", MvcboardVO.class);
		mvcboardVO.setIdx(idx);
		mvcboardVO.setName(name);
		mvcboardVO.setSubject(subject);
		mvcboardVO.setContent(content);
		mvcboardVO.setRef(ref);
		mvcboardVO.setLev(lev + 1);
		mvcboardVO.setSeq(seq + 1);

//		답글이 삽입될 위치를 정하기 위해 조건에 만족하는 seq를 1씩 증가시키는 메소드를 실행한다.
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		hmap.put("ref", mvcboardVO.getRef());
		hmap.put("seq", mvcboardVO.getSeq());
		mapper.replyIncrement(hmap);
//		답글을 저장하는 메소드를 실행한다.
		mapper.replyInsert(mvcboardVO);		

//		답글 저장 후 돌아갈 페이지 번호를 Model 인터페이스 객체에 저장한다.
		model.addAttribute("currentPage", Integer.parseInt(request.getParameter("currentPage")));
		
		return "redirect:list";
	}
	
}



















