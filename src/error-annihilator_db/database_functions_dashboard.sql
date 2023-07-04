-- Dashboard Functions ----------------------------------------------------------------------------------------------------

-- Function -- Count all tickets submitted per day (timeline chart) --
    CREATE OR REPLACE FUNCTION count_tickets_per_day()
    RETURNS TABLE (submission_date DATE, ticket_count INTEGER) AS $$
    BEGIN
        RETURN QUERY
        SELECT DATE_TRUNC('day', created) AS submission_date, COUNT(*) AS ticket_count
        FROM tickets
        GROUP BY submission_date
        ORDER BY submission_date;
    END;
    $$ LANGUAGE plpgsql;

-- Function -- Get an average of what ticket types come in every day of the week --
    CREATE OR REPLACE FUNCTION average_ticket_types_per_day_of_week()
    RETURNS TABLE (day_of_week INTEGER, ticket_type_id INTEGER, average_count FLOAT) AS $$
    BEGIN
        RETURN QUERY
        SELECT EXTRACT(ISODOW FROM created) AS day_of_week, type_id, AVG(count) AS average_count
        FROM (
             SELECT DATE_TRUNC('day', created) AS submission_date, type_id, COUNT(*) AS count
             FROM tickets
             GROUP BY submission_date, type_id
         ) AS ticket_counts
        GROUP BY day_of_week, type_id
        ORDER BY day_of_week, type_id;
    END;
    $$ LANGUAGE plpgsql;

-- Function -- Count how many tickets there are per status (except for status closed/resolved) --
    CREATE OR REPLACE FUNCTION count_tickets_per_status()
    RETURNS TABLE (status_id INTEGER, status_name VARCHAR(255), ticket_count INTEGER) AS $$
    BEGIN
        RETURN QUERY
        SELECT s.status_id, s.status_name, COUNT(*) AS ticket_count
        FROM tickets t
             JOIN status s ON t.status_id = s.status_id
        WHERE s.status_name != 'Closed' AND s.status_name != 'Resolved'
        GROUP BY s.status_id, s.status_name
        ORDER BY s.status_id;
    END;
    $$ LANGUAGE plpgsql;

-- Function -- Count how many tickets there are per type --
    CREATE OR REPLACE FUNCTION count_tickets_per_type()
    RETURNS TABLE (type_id INTEGER, type_name VARCHAR(255), ticket_count INTEGER) AS $$
    BEGIN
        RETURN QUERY
        SELECT t.type_id, t.type_name, COUNT(*) AS ticket_count
        FROM tickets ti
             JOIN types t ON ti.type_id = t.type_id
        GROUP BY t.type_id, t.type_name
        ORDER BY t.type_id;
    END;
    $$ LANGUAGE plpgsql;

-- Function -- Calculate the average turnaround time for resolved tickets of each type --
    CREATE OR REPLACE FUNCTION average_turnaround_time_per_type()
    RETURNS TABLE (type_id INTEGER, type_name VARCHAR(255), average_turnaround INTERVAL) AS $$
    BEGIN
        RETURN QUERY
        SELECT t.type_id, t.type_name, AVG(ti.resolved - ti.created) AS average_turnaround
        FROM tickets ti
            JOIN types t ON ti.type_id = t.type_id
        WHERE ti.resolved IS NOT NULL
        GROUP BY t.type_id, t.type_name
        ORDER BY t.type_id;
    END;
    $$ LANGUAGE plpgsql;

-- Function -- Count all tickets resolved per day (timeline chart) --
    CREATE OR REPLACE FUNCTION count_resolved_tickets_per_day()
    RETURNS TABLE (resolved_date DATE, ticket_count INTEGER) AS $$
    BEGIN
        RETURN QUERY
        SELECT DATE_TRUNC('day', resolved) AS resolved_date, COUNT(*) AS ticket_count
        FROM tickets
        WHERE resolved IS NOT NULL
        GROUP BY resolved_date
        ORDER BY resolved_date;
    END;
    $$ LANGUAGE plpgsql;
 







