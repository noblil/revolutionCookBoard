package kr.koreait.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import kr.koreait.springMVCBoard_DBCP.Constant;
import kr.koreait.vo.MvcboardVO;

//	mybatis는 DAO 클래스에서 sql 명령을 실행하지 않고 sql 명령을 실행하는데 필요한 데이터를 컨트롤러에서 준비하고 인터페이스를 하나 만들어서
//	sql 명령을 실행하는 xml 파일과 연결한다.
public class MvcboardDAO {

	private JdbcTemplate template;
	
	public MvcboardDAO() {
		template = Constant.template;
	}

//	InsertService 클래스에서 테이블에 저장할 메인글 데이터가 저장된 객체를 넘겨받고 insert sql 명령을 실행하는 메소드
	public void insert(final MvcboardVO mvcboardVO) {
		System.out.println("MvcboardDAO 클래스의 insert() 메소드 실행");
		String sql = "insert into mvcboard (idx, name, subject, content, ref, lev, seq) " + 
				"values (mvcboard_idx_seq.nextval, ?, ?, ?, mvcboard_idx_seq.currval, 0, 0)";
		template.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, mvcboardVO.getName());
				ps.setString(2, mvcboardVO.getSubject());
				ps.setString(3, mvcboardVO.getContent());
				
			}
		});
	}

//	SelectService 클래스에서 호출되는 테이블에 저장된 전체 글의 개수를 얻어오는 select sql 명령을 실행하는 메소드
	public int selectCount() {
		System.out.println("MvcboardDAO 클래스의 selectCount() 메소드 실행");
		String sql = "select count(*) from mvcboard";
		return template.queryForInt(sql);
	}

//	SelectService 클래스에서 호출되는 브라우저 화면에 표시할 한 페이지 분량의 시작 인덱스 번호와 끝 인덱스 번호가 저장된 HashMap 객체를 넘겨받고
//	테이블에서 한 페이지 분량의 글을 얻어오는 select sql 명령을 실행하는 메소드
	public ArrayList<MvcboardVO> selectList(HashMap<String, Integer> hmap) {
		System.out.println("MvcboardDAO 클래스의 selectList() 메소드 실행");
		String sql = "select * from (" +
	             "    select rownum rnum, TT.* from (" +
			     "        select * from mvcboard order by ref desc, seq asc" +
	             "    ) TT where rownum <= " + hmap.get("endNo") + 
			     ") where rnum >= " + hmap.get("startNo");
		return (ArrayList<MvcboardVO>) template.query(sql, new BeanPropertyRowMapper(MvcboardVO.class));
	}

//	IncrementService 클래스에서 조회수를 증가시킬 글 번호를 넘겨받고 조회수를 증가시키는 update sql 명령을 실행하는 메소드
	public void increment(final int idx) {
		System.out.println("MvcboardDAO 클래스의 increment() 메소드 실행");
		String sql = "update mvcboard set hit = hit + 1 where idx = ?";
		template.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, idx);
			}
		});
	}

//	ContentViewService 클래스에서 조회수를 증가시킨 글 번호를 넘겨받고 조회수를 증가시킨 글 한 건을 얻어오는 select sql 명령을 실행하는 메소드
	public MvcboardVO selectByIdx(int idx) {
		System.out.println("MvcboardDAO 클래스의 selectByIdx() 메소드 실행");
		String sql = "select * from mvcboard where idx = " + idx;
		return template.queryForObject(sql, new BeanPropertyRowMapper(MvcboardVO.class));
	}

//	DeleteService 클래스에서 삭제할 글 번호를 넘겨받고 글 한 건을 삭제하는 delete sql 명령을 실행하는 메소드
	public void delete(final int idx) {
		System.out.println("MvcboardDAO 클래스의 delete() 메소드 실행");
		String sql = "delete from mvcboard where idx = ?";
		template.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, idx);
			}
		});
	}

//	UpdateService 클래스에서 호출되는 수정할 글 번호와 수정할 데이터를 넘겨받고 글 한 건을 수정하는 update sql 명령을 실행하는 메소드
	public void update(final int idx, final String subject, final String content) {
		System.out.println("MvcboardDAO 클래스의 update() 메소드 실행");
		String sql = "update mvcboard set subject = ?, content = ? where idx = ?";
		template.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, subject);
				ps.setString(2, content);
				ps.setInt(3, idx);
			}
		});
	}

//	UpdateService 클래스에서 호출되는 수정할 글이 저장된 객체를 넘겨받고 글 한 건을 수정하는 update sql 명령을 실행하는 메소드
	public void update(final MvcboardVO mvcboardVO) {
		System.out.println("MvcboardDAO 클래스의 update() 메소드 실행");
		String sql = "update mvcboard set subject = ?, content = ? where idx = ?";
		template.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, mvcboardVO.getSubject());
				ps.setString(2, mvcboardVO.getContent());
				ps.setInt(3, mvcboardVO.getIdx());
			}
		});
	}

//	ReplyService 클래스에서 글 그룹(ref)과 글 그룹에서 글이 출력되는 순서(seq)가 저장된 HashMap 객체를 넘겨받고 조건에 만족하는 seq를 1씩
//	증가시키는 update sql 명령을 실행하는 메소드
	public void replyIncrement(final HashMap<String, Integer> hmap) {
		System.out.println("MvcboardDAO 클래스의 replyIncrement() 메소드 실행");
		String sql = "update mvcboard set seq = seq + 1 where ref = ? and seq >= ?";
		template.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, hmap.get("ref"));
				ps.setInt(2, hmap.get("seq"));
			}
		});
	}

//	ReplyService 클래스에서 답글이 저장된 객체를 넘겨받고 답글을 저장하는 insert sql 명령을 실행하는 메소드
	public void replyInsert(final MvcboardVO mvcboardVO) {
		System.out.println("MvcboardDAO 클래스의 replyInsert() 메소드 실행");
		String sql = "insert into mvcboard (idx, name, subject, content, ref, lev, seq) values (mvcboard_idx_seq.nextval, ?, ?, ?, ?, ?, ?)";
		template.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, mvcboardVO.getName());
				ps.setString(2, mvcboardVO.getSubject());
				ps.setString(3, mvcboardVO.getContent());
				ps.setInt(4, mvcboardVO.getRef());
				ps.setInt(5, mvcboardVO.getLev());
				ps.setInt(6, mvcboardVO.getSeq());
			}
		});
	}
	
}









