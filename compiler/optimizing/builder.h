/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef ART_COMPILER_OPTIMIZING_BUILDER_H_
#define ART_COMPILER_OPTIMIZING_BUILDER_H_

#include "base/arena_containers.h"
#include "base/arena_object.h"
#include "block_builder.h"
#include "dex_file-inl.h"
#include "dex_file.h"
#include "driver/compiler_driver.h"
#include "driver/dex_compilation_unit.h"
#include "instruction_builder.h"
#include "nodes.h"
#include "optimizing_compiler_stats.h"
#include "primitive.h"
#include "ssa_builder.h"

namespace art {

class CodeGenerator;

class HGraphBuilder : public ValueObject {
 public:
  HGraphBuilder(HGraph* graph,
                DexCompilationUnit* dex_compilation_unit,
                const DexCompilationUnit* const outer_compilation_unit,
                const DexFile* dex_file,
                const DexFile::CodeItem& code_item,
                CompilerDriver* driver,
                CodeGenerator* code_generator,
                OptimizingCompilerStats* compiler_stats,
                const uint8_t* interpreter_metadata,
                Handle<mirror::DexCache> dex_cache,
                VariableSizedHandleScope* handles)
      : graph_(graph),
        dex_file_(dex_file),
        code_item_(code_item),
        dex_compilation_unit_(dex_compilation_unit),
        compiler_driver_(driver),
        compilation_stats_(compiler_stats),
        block_builder_(graph, dex_file, code_item),
        ssa_builder_(graph,
                     dex_compilation_unit->GetClassLoader(),
                     dex_compilation_unit->GetDexCache(),
                     handles),
        instruction_builder_(graph,
                             &block_builder_,
                             &ssa_builder_,
                             dex_file,
                             code_item_,
                             Primitive::GetType(dex_compilation_unit_->GetShorty()[0]),
                             dex_compilation_unit,
                             outer_compilation_unit,
                             driver,
                             code_generator,
                             interpreter_metadata,
                             compiler_stats,
                             dex_cache,
                             handles) {}

  // Only for unit testing.
  HGraphBuilder(HGraph* graph,
                const DexFile::CodeItem& code_item,
                VariableSizedHandleScope* handles,
                Primitive::Type return_type = Primitive::kPrimInt)
      : graph_(graph),
        dex_file_(nullptr),
        code_item_(code_item),
        dex_compilation_unit_(nullptr),
        compiler_driver_(nullptr),
        compilation_stats_(nullptr),
        block_builder_(graph, nullptr, code_item),
        ssa_builder_(graph,
                     handles->NewHandle<mirror::ClassLoader>(nullptr),
                     handles->NewHandle<mirror::DexCache>(nullptr),
                     handles),
        instruction_builder_(graph,
                             &block_builder_,
                             &ssa_builder_,
                             /* dex_file */ nullptr,
                             code_item_,
                             return_type,
                             /* dex_compilation_unit */ nullptr,
                             /* outer_compilation_unit */ nullptr,
                             /* compiler_driver */ nullptr,
                             /* code_generator */ nullptr,
                             /* interpreter_metadata */ nullptr,
                             /* compiler_stats */ nullptr,
                             handles->NewHandle<mirror::DexCache>(nullptr),
                             handles) {}

  GraphAnalysisResult BuildGraph();

  static constexpr const char* kBuilderPassName = "builder";

 private:
  bool SkipCompilation(size_t number_of_branches);

  HGraph* const graph_;
  const DexFile* const dex_file_;
  const DexFile::CodeItem& code_item_;

  // The compilation unit of the current method being compiled. Note that
  // it can be an inlined method.
  DexCompilationUnit* const dex_compilation_unit_;

  CompilerDriver* const compiler_driver_;

  OptimizingCompilerStats* compilation_stats_;

  HBasicBlockBuilder block_builder_;
  SsaBuilder ssa_builder_;
  HInstructionBuilder instruction_builder_;

  DISALLOW_COPY_AND_ASSIGN(HGraphBuilder);
};

}  // namespace art

#endif  // ART_COMPILER_OPTIMIZING_BUILDER_H_
