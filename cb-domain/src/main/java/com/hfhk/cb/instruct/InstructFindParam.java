package com.hfhk.cb.instruct;

import com.hfhk.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructFindParam extends AbstractPage<InstructFindParam> implements Serializable {
	private String keyword;

	private Set<InstructType> type;

	private Set<String> projects;

	private List<String> designateAuditUsers;
}
