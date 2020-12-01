package edu.pasudo123.study.demo.member.mapper;

import edu.pasudo123.study.demo.member.model.Member;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        final Member currentMember = Member.builder()
                .name(rs.getString("name"))
                .status(rs.getString("status"))
                .build();
        currentMember.updateId(rs.getLong("id"));
        return currentMember;
    }
}
