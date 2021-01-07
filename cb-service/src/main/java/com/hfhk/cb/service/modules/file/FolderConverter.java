package com.hfhk.cb.service.modules.file;

import com.hfhk.cairo.core.tree.TreeConverter;
import com.hfhk.cb.file.domain.Folder;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FolderConverter {
	public static final String FOLDER_TREE_ROOT = "/";
	public static final String SPLIT = "/";



	public static Optional<Folder> optionalFolder(String path) {
		return Optional.ofNullable(path)
			.filter(x -> x.contains(SPLIT))
			.map(x -> {
				String parentId = x.substring(0, x.lastIndexOf(SPLIT));
				return Folder.builder()
					.id(x)
					.parentId(parentId.isEmpty() ? FOLDER_TREE_ROOT : parentId)
					.build();
			});
	}

	public static List<Folder> tree(Collection<Folder> folders) {
		return TreeConverter.build(folders, FOLDER_TREE_ROOT, Comparator.comparing(Folder::getId));
	}

	public static List<Folder> tree(Collection<Folder> folders, String ROOT) {
		return TreeConverter.build(folders, ROOT, Comparator.comparing(Folder::getId));
	}
}
