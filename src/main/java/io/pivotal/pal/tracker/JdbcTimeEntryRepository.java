package io.pivotal.pal.tracker;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    //public JdbcTimeEntryRepository(){};
    public JdbcTimeEntryRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "insert into time_entries (project_id, user_id, date, hours)" +
                " values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());
                    return ps;
                }, keyHolder);
        //System.out.println(keyHolder.getKey());
        TimeEntry newTimeEntry = new TimeEntry(keyHolder.getKey().longValue(),timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours());
        return newTimeEntry;
        //return this.find(keyHolder.getKey()))
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        //String sql = "select * from time_entries where id = ?";
        //TimeEntry newTimeEntry = jdbcTemplate.query(sql,new Object[]{timeEntryId},(resultSet, i) ->{
        //    return new TimeEntry timeEntry(
        //            resultSet.getLong("id"),
        //            resultSet.getLong("project_id"),
        //            resultSet.getLong("user_id"),
        //            new java.sql.Date(resultSet.getDate("date").getTime()).toLocalDate(),
        //            resultSet.getInt("hours"));
        //});
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{timeEntryId},
                extractor);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("select * from time_entries",mapper);
    }

    @Override
    public TimeEntry update(long eq, TimeEntry timeEntry) {
        String sql = "update time_entries set project_id=?, user_id=?, date=?, hours=? where id = ?";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
                    ps.setLong(1, timeEntry.getProjectId());
                    ps.setLong(2, timeEntry.getUserId());
                    ps.setDate(3, Date.valueOf(timeEntry.getDate()));
                    ps.setInt(4, timeEntry.getHours());
                    ps.setLong(5, eq);
                    return ps;
                });
        //System.out.println(keyHolder.getKey());
        //TimeEntry newTimeEntry = new TimeEntry(eq,timeEntry.getProjectId(),timeEntry.getUserId(),timeEntry.getDate(),timeEntry.getHours());
        return find(eq);
    }

    @Override
    public void delete(long timeEntryId) {
        String sql = "delete from time_entries where id=?";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
                    ps.setLong(1, timeEntryId);
                    return ps;
                });
    }
    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );
    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;
}
