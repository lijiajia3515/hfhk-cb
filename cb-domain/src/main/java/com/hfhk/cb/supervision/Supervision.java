package com.hfhk.cb.supervision;

import com.hfhk.auth.Metadata;
import com.hfhk.cb.project.Project;
import com.hfhk.system.file.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supervision {
	private String id;
	private List<File> files;
	private Project project;
	private String unit;
	private Long problemNumber;
	private Metadata metadata;
}
