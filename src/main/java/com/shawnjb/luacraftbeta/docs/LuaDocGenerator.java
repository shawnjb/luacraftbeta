package com.shawnjb.luacraftbeta.docs;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LuaDocGenerator {
    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        Path outputPath = Paths.get("src/main/resources/docs/docs.lua");

        try {
            Files.createDirectories(outputPath.getParent());

            LuaDocBootstrap.registerAll();

            try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
                writer.write("---@meta\n");
                writer.write("-- Auto-generated LuaCATS documentation\n");
                writer.write("-- Generated by LuaDocGenerator for LuaCraftBeta\n\n");

                Set<String> classNames = LuaDocRegistry.getAllClasses();
                for (String className : classNames) {
                    writer.write(String.format("---@class %s\n", className));
                    writer.write(String.format("local %s = {}\n\n", className));
                }

                Map<String, List<LuaDocRegistry.FunctionDoc>> docs = LuaDocRegistry.getAllFunctions();
                for (Map.Entry<String, List<LuaDocRegistry.FunctionDoc>> entry : docs.entrySet()) {
                    String category = entry.getKey();
                    boolean isGlobal = category.equals("core");

                    if (!isGlobal) {
                        writer.write(String.format("---@class %s\n", category));
                        writer.write(String.format("local %s = {}\n\n", category));
                    }

                    for (LuaDocRegistry.FunctionDoc func : entry.getValue()) {
                        writer.write(String.format("---%s\n", func.description));

                        for (LuaDocRegistry.Param param : func.params) {
                            writer.write(String.format("---@param %s %s\n", param.name, param.type));
                        }

                        for (LuaDocRegistry.Return ret : func.returns) {
                            if (ret.description != null && !ret.description.isEmpty()) {
                                writer.write(String.format("---@return %s @%s\n", ret.type, ret.description));
                            } else {
                                writer.write(String.format("---@return %s\n", ret.type));
                            }
                        }

                        if (isGlobal) {
                            writer.write(String.format("function %s(", func.name));
                        } else {
                            writer.write(String.format("function %s.%s(", category, func.name));
                        }

                        for (int i = 0; i < func.params.size(); i++) {
                            writer.write(func.params.get(i).name);
                            if (i < func.params.size() - 1)
                                writer.write(", ");
                        }
                        writer.write(") end\n\n");
                    }
                }

                System.out.println("[LuaDocGenerator] docs.lua generated at: " + outputPath.toAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("[LuaDocGenerator] Failed to generate docs.lua");
            e.printStackTrace();
        }
    }
}
