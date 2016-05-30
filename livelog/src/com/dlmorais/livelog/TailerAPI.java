package com.dlmorais.livelog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tail")
@Produces(MediaType.APPLICATION_JSON)
public class TailerAPI {

	@GET
	public Response get(@QueryParam("f") String file, 
						@QueryParam("l") Integer fromLine,
						@QueryParam("n") Integer numberOfLines) throws IOException {
		
		Integer numLines = Optional.ofNullable(numberOfLines).orElse(100);
		
		final List<LogLineDTO> content = new ArrayList<>();
		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(LiveLogConfig.getLogDir() + file));
			
			long startLine = 0;
			if (fromLine != null) {
				startLine = fromLine - 1;
			} else {
				startLine = lines.parallel().mapToLong(l -> 1L).sum() - numLines;
				lines.close();
				lines = Files.lines(Paths.get(LiveLogConfig.getLogDir() + file));
			}
			
			long line = Math.max(startLine, 0);
			lines.skip(line).forEach(l -> {
				LogLineDTO dto = new LogLineDTO();
				dto.setContent(l);
				content.add(dto);
			});
			
			for (LogLineDTO logLineDTO : content) {
				logLineDTO.setLine(++line);
			}
		} finally {
			if (lines != null) lines.close();
		}
		
		return Response.ok(content).build();
	}
	
}
