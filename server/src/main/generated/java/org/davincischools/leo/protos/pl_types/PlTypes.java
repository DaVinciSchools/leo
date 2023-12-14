// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pl_types.proto

package org.davincischools.leo.protos.pl_types;

public final class PlTypes {
  private PlTypes() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_Assignment_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_Assignment_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ClassX_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ClassX_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_District_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_District_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_KnowledgeAndSkill_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_KnowledgeAndSkill_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_Project_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_Project_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_Project_Milestone_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_Project_Milestone_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_Project_Milestone_Step_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_Project_Milestone_Step_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_School_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_School_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_UserX_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_UserX_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectDefinition_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectDefinition_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectInputValue_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectInputValue_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectInputCategory_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectInputCategory_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectInputCategory_Option_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectInputCategory_Option_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_Tag_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_Tag_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectPostComment_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectPostComment_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectPostRatingCategory_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectPostRatingCategory_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectPostRating_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectPostRating_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_pl_types_ProjectPost_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_pl_types_ProjectPost_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016pl_types.proto\022\010pl_types\"\351\002\n\nAssignmen" +
      "t\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022\021\n\004name\030\003 \001(\tH\001\210\001\001\022\025\n" +
      "\010nickname\030\006 \001(\tH\002\210\001\001\022\030\n\013short_descr\030\004 \001(" +
      "\tH\003\210\001\001\022\034\n\017long_descr_html\030\005 \001(\tH\004\210\001\001\022&\n\007" +
      "class_x\030\002 \001(\0132\020.pl_types.ClassXH\005\210\001\001\0229\n\024" +
      "knowledge_and_skills\030\007 \003(\0132\033.pl_types.Kn" +
      "owledgeAndSkill\0228\n\023project_definitions\030\010" +
      " \003(\0132\033.pl_types.ProjectDefinitionB\005\n\003_id" +
      "B\007\n\005_nameB\013\n\t_nicknameB\016\n\014_short_descrB\022" +
      "\n\020_long_descr_htmlB\n\n\010_class_x\"\216\003\n\006Class" +
      "X\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022%\n\006school\030\t \001(\0132\020.pl_" +
      "types.SchoolH\001\210\001\001\022\021\n\004name\030\002 \001(\tH\002\210\001\001\022\023\n\006" +
      "number\030\006 \001(\tH\003\210\001\001\022\023\n\006period\030\007 \001(\tH\004\210\001\001\022\022" +
      "\n\005grade\030\010 \001(\tH\005\210\001\001\022\030\n\013short_descr\030\003 \001(\tH" +
      "\006\210\001\001\022\034\n\017long_descr_html\030\004 \001(\tH\007\210\001\001\022)\n\013as" +
      "signments\030\013 \003(\0132\024.pl_types.Assignment\0229\n" +
      "\024knowledge_and_skills\030\005 \003(\0132\033.pl_types.K" +
      "nowledgeAndSkillB\005\n\003_idB\t\n\007_schoolB\007\n\005_n" +
      "ameB\t\n\007_numberB\t\n\007_periodB\010\n\006_gradeB\016\n\014_" +
      "short_descrB\022\n\020_long_descr_html\">\n\010Distr" +
      "ict\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022\021\n\004name\030\002 \001(\tH\001\210\001\001B" +
      "\005\n\003_idB\007\n\005_name\"\231\003\n\021KnowledgeAndSkill\022\017\n" +
      "\002id\030\001 \001(\005H\000\210\001\001\0223\n\004type\030\002 \001(\0162 .pl_types." +
      "KnowledgeAndSkill.TypeH\001\210\001\001\022\021\n\004name\030\003 \001(" +
      "\tH\002\210\001\001\022\025\n\010category\030\006 \001(\tH\003\210\001\001\022\030\n\013short_d" +
      "escr\030\004 \001(\tH\004\210\001\001\022\034\n\017long_descr_html\030\005 \001(\t" +
      "H\005\210\001\001\022\023\n\006global\030\007 \001(\010H\006\210\001\001\022$\n\006user_x\030\t \001" +
      "(\0132\017.pl_types.UserXH\007\210\001\001\";\n\004Type\022\016\n\nUNSE" +
      "T_TYPE\020\000\022\007\n\003EKS\020\001\022\021\n\rXQ_COMPETENCY\020\002\022\007\n\003" +
      "CTE\020\003B\005\n\003_idB\007\n\005_typeB\007\n\005_nameB\013\n\t_categ" +
      "oryB\016\n\014_short_descrB\022\n\020_long_descr_htmlB" +
      "\t\n\007_globalB\t\n\007_user_xJ\004\010\010\020\t\"\300\006\n\007Project\022" +
      "\017\n\002id\030\001 \001(\005H\000\210\001\001\022\021\n\004name\030\002 \001(\tH\001\210\001\001\022\030\n\013s" +
      "hort_descr\030\003 \001(\tH\002\210\001\001\022\034\n\017long_descr_html" +
      "\030\004 \001(\tH\003\210\001\001\022\025\n\010favorite\030\005 \001(\010H\004\210\001\001\0228\n\014th" +
      "umbs_state\030\006 \001(\0162\035.pl_types.Project.Thum" +
      "bsStateH\005\210\001\001\022 \n\023thumbs_state_reason\030\007 \001(" +
      "\tH\006\210\001\001\022\025\n\010archived\030\010 \001(\010H\007\210\001\001\022\023\n\006active\030" +
      "\n \001(\010H\010\210\001\001\022-\n\nassignment\030\013 \001(\0132\024.pl_type" +
      "s.AssignmentH\t\210\001\001\022<\n\022project_definition\030" +
      "\r \001(\0132\033.pl_types.ProjectDefinitionH\n\210\001\001\022" +
      "/\n\nmilestones\030\014 \003(\0132\033.pl_types.Project.M" +
      "ilestone\032\254\001\n\tMilestone\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022" +
      "\021\n\004name\030\002 \001(\tH\001\210\001\001\022/\n\005steps\030\003 \003(\0132 .pl_t" +
      "ypes.Project.Milestone.Step\032:\n\004Step\022\017\n\002i" +
      "d\030\001 \001(\005H\000\210\001\001\022\021\n\004name\030\002 \001(\tH\001\210\001\001B\005\n\003_idB\007" +
      "\n\005_nameB\005\n\003_idB\007\n\005_name\"E\n\013ThumbsState\022\026" +
      "\n\022UNSET_THUMBS_STATE\020\000\022\r\n\tTHUMBS_UP\020\001\022\017\n" +
      "\013THUMBS_DOWN\020\002B\005\n\003_idB\007\n\005_nameB\016\n\014_short" +
      "_descrB\022\n\020_long_descr_htmlB\013\n\t_favoriteB" +
      "\017\n\r_thumbs_stateB\026\n\024_thumbs_state_reason" +
      "B\013\n\t_archivedB\t\n\007_activeB\r\n\013_assignmentB" +
      "\025\n\023_project_definition\"\315\001\n\006School\022\017\n\002id\030" +
      "\001 \001(\005H\000\210\001\001\022)\n\010district\030\006 \001(\0132\022.pl_types." +
      "DistrictH\001\210\001\001\022\021\n\004name\030\003 \001(\tH\002\210\001\001\022\025\n\010nick" +
      "name\030\004 \001(\tH\003\210\001\001\022\024\n\007address\030\005 \001(\tH\004\210\001\001B\005\n" +
      "\003_idB\013\n\t_districtB\007\n\005_nameB\013\n\t_nicknameB" +
      "\n\n\010_addressJ\004\010\002\020\003R\013district_id\"\213\004\n\005UserX" +
      "\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022\030\n\013district_id\030\002 \001(\005H\001" +
      "\210\001\001\022\027\n\nfirst_name\030\003 \001(\tH\002\210\001\001\022\026\n\tlast_nam" +
      "e\030\004 \001(\tH\003\210\001\001\022\032\n\remail_address\030\005 \001(\tH\004\210\001\001" +
      "\022\027\n\nis_admin_x\030\006 \001(\010H\005\210\001\001\022\027\n\nadmin_x_id\030" +
      "\007 \001(\005H\006\210\001\001\022\027\n\nis_teacher\030\010 \001(\010H\007\210\001\001\022\027\n\nt" +
      "eacher_id\030\t \001(\005H\010\210\001\001\022\027\n\nis_student\030\n \001(\010" +
      "H\t\210\001\001\022\027\n\nstudent_id\030\013 \001(\005H\n\210\001\001\022\024\n\007is_dem" +
      "o\030\014 \001(\010H\013\210\001\001\022\035\n\020is_authenticated\030\r \001(\010H\014" +
      "\210\001\001B\005\n\003_idB\016\n\014_district_idB\r\n\013_first_nam" +
      "eB\014\n\n_last_nameB\020\n\016_email_addressB\r\n\013_is" +
      "_admin_xB\r\n\013_admin_x_idB\r\n\013_is_teacherB\r" +
      "\n\013_teacher_idB\r\n\013_is_studentB\r\n\013_student" +
      "_idB\n\n\010_is_demoB\023\n\021_is_authenticated\"\354\005\n" +
      "\021ProjectDefinition\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022\021\n\004n" +
      "ame\030\002 \001(\tH\001\210\001\001\022\025\n\010input_id\030\007 \001(\005H\002\210\001\001\022\025\n" +
      "\010template\030\003 \001(\010H\003\210\001\001\022\025\n\010selected\030\004 \001(\010H\004" +
      "\210\001\001\0225\n\005state\030\005 \001(\0162!.pl_types.ProjectDef" +
      "inition.StateH\005\210\001\001\0220\n\020existing_project\030\t" +
      " \001(\0132\021.pl_types.ProjectH\006\210\001\001\022Z\n\031existing" +
      "_project_use_type\030\n \001(\01622.pl_types.Proje" +
      "ctDefinition.ExistingProjectUseTypeH\007\210\001\001" +
      "\022+\n\006inputs\030\006 \003(\0132\033.pl_types.ProjectInput" +
      "Value\022-\n\nassignment\030\010 \001(\0132\024.pl_types.Ass" +
      "ignmentH\010\210\001\001\"C\n\005State\022\017\n\013UNSET_STATE\020\000\022\016" +
      "\n\nPROCESSING\020\001\022\r\n\tCOMPLETED\020\002\022\n\n\006FAILED\020" +
      "\003\"\204\001\n\026ExistingProjectUseType\022-\n)UNSET_EX" +
      "ISTING_PROJECT_CONFIGURATION_TYPE\020\000\022\025\n\021U" +
      "SE_CONFIGURATION\020\001\022\022\n\016MORE_LIKE_THIS\020\002\022\020" +
      "\n\014SUB_PROJECTS\020\003B\005\n\003_idB\007\n\005_nameB\013\n\t_inp" +
      "ut_idB\013\n\t_templateB\013\n\t_selectedB\010\n\006_stat" +
      "eB\023\n\021_existing_projectB\034\n\032_existing_proj" +
      "ect_use_typeB\r\n\013_assignment\"\207\001\n\021ProjectI" +
      "nputValue\0225\n\010category\030\002 \001(\0132\036.pl_types.P" +
      "rojectInputCategoryH\000\210\001\001\022\022\n\nfree_texts\030\003" +
      " \003(\t\022\024\n\014selected_ids\030\004 \003(\005B\013\n\t_categoryJ" +
      "\004\010\001\020\002\"\353\005\n\024ProjectInputCategory\022\017\n\002id\030\001 \001" +
      "(\005H\000\210\001\001\022\024\n\007type_id\030\002 \001(\005H\001\210\001\001\022\021\n\004name\030\003 " +
      "\001(\tH\002\210\001\001\022\030\n\013short_descr\030\004 \001(\tH\003\210\001\001\022\030\n\013in" +
      "put_descr\030\n \001(\tH\004\210\001\001\022\021\n\004hint\030\005 \001(\tH\005\210\001\001\022" +
      "\030\n\013placeholder\030\006 \001(\tH\006\210\001\001\022A\n\nvalue_type\030" +
      "\007 \001(\0162(.pl_types.ProjectInputCategory.Va" +
      "lueTypeH\007\210\001\001\022\033\n\016max_num_values\030\010 \001(\005H\010\210\001" +
      "\001\0226\n\007options\030\t \003(\0132%.pl_types.ProjectInp" +
      "utCategory.Option\032\301\001\n\006Option\022\017\n\002id\030\001 \001(\005" +
      "H\000\210\001\001\022\021\n\004name\030\002 \001(\tH\001\210\001\001\022\025\n\010category\030\004 \001" +
      "(\tH\002\210\001\001\022\030\n\013short_descr\030\003 \001(\tH\003\210\001\001\022$\n\006use" +
      "r_x\030\006 \001(\0132\017.pl_types.UserXH\004\210\001\001B\005\n\003_idB\007" +
      "\n\005_nameB\013\n\t_categoryB\016\n\014_short_descrB\t\n\007" +
      "_user_xJ\004\010\005\020\006\"e\n\tValueType\022\024\n\020UNSET_VALU" +
      "E_TYPE\020\000\022\r\n\tFREE_TEXT\020\001\022\007\n\003EKS\020\002\022\021\n\rXQ_C" +
      "OMPETENCY\020\003\022\016\n\nMOTIVATION\020\004\022\007\n\003CTE\020\005B\005\n\003" +
      "_idB\n\n\010_type_idB\007\n\005_nameB\016\n\014_short_descr" +
      "B\016\n\014_input_descrB\007\n\005_hintB\016\n\014_placeholde" +
      "rB\r\n\013_value_typeB\021\n\017_max_num_values\"G\n\003T" +
      "ag\022\021\n\004text\030\001 \001(\tH\000\210\001\001\022\026\n\tuser_x_id\030\002 \001(\005" +
      "H\001\210\001\001B\007\n\005_textB\014\n\n_user_x_id\"\301\002\n\022Project" +
      "PostComment\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022$\n\006user_x\030\002" +
      " \001(\0132\017.pl_types.UserXH\001\210\001\001\0220\n\014project_po" +
      "st\030\013 \001(\0132\025.pl_types.ProjectPostH\002\210\001\001\022\034\n\017" +
      "long_descr_html\030\004 \001(\tH\003\210\001\001\022\031\n\014post_time_" +
      "ms\030\010 \001(\003H\004\210\001\001\022\031\n\014being_edited\030\t \001(\010H\005\210\001\001" +
      "B\005\n\003_idB\t\n\007_user_xB\017\n\r_project_postB\022\n\020_" +
      "long_descr_htmlB\017\n\r_post_time_msB\017\n\r_bei" +
      "ng_editedJ\004\010\n\020\013R\017project_post_id\"\373\001\n\031Pro" +
      "jectPostRatingCategory\022)\n\034project_input_" +
      "fulfillment_id\030\001 \001(\005H\000\210\001\001\022\025\n\010category\030\002 " +
      "\001(\tH\001\210\001\001\022\022\n\005value\030\003 \001(\tH\002\210\001\001\022A\n\nvalue_ty" +
      "pe\030\004 \001(\0162(.pl_types.ProjectInputCategory" +
      ".ValueTypeH\003\210\001\001B\037\n\035_project_input_fulfil" +
      "lment_idB\013\n\t_categoryB\010\n\006_valueB\r\n\013_valu" +
      "e_type\"\204\004\n\021ProjectPostRating\022\017\n\002id\030\001 \001(\005" +
      "H\000\210\001\001\022$\n\006user_x\030\002 \001(\0132\017.pl_types.UserXH\001" +
      "\210\001\001\022\023\n\006rating\030\003 \001(\005H\002\210\001\001\022@\n\013rating_type\030" +
      "\004 \001(\0162&.pl_types.ProjectPostRating.Ratin" +
      "gTypeH\003\210\001\001\0220\n\014project_post\030\005 \001(\0132\025.pl_ty" +
      "pes.ProjectPostH\004\210\001\001\022=\n\023knowledge_and_sk" +
      "ill\030\006 \001(\0132\033.pl_types.KnowledgeAndSkillH\005" +
      "\210\001\001\022)\n\034project_input_fulfillment_id\030\007 \001(" +
      "\005H\006\210\001\001\"N\n\nRatingType\022\025\n\021UNSET_RATING_TYP" +
      "E\020\000\022\022\n\016INITIAL_1_TO_5\020\001\022\025\n\021GOAL_COMPLETE" +
      "_PCT\020\002B\005\n\003_idB\t\n\007_user_xB\t\n\007_ratingB\016\n\014_" +
      "rating_typeB\017\n\r_project_postB\026\n\024_knowled" +
      "ge_and_skillB\037\n\035_project_input_fulfillme" +
      "nt_id\"\262\004\n\013ProjectPost\022\017\n\002id\030\001 \001(\005H\000\210\001\001\022$" +
      "\n\006user_x\030\002 \001(\0132\017.pl_types.UserXH\001\210\001\001\022\'\n\007" +
      "project\030\014 \001(\0132\021.pl_types.ProjectH\002\210\001\001\022\021\n" +
      "\004name\030\003 \001(\tH\003\210\001\001\022\034\n\017long_descr_html\030\004 \001(" +
      "\tH\004\210\001\001\022\035\n\020desired_feedback\030\006 \001(\tH\005\210\001\001\022\033\n" +
      "\004tags\030\007 \003(\0132\r.pl_types.Tag\022.\n\010comments\030\013" +
      " \003(\0132\034.pl_types.ProjectPostComment\022\031\n\014po" +
      "st_time_ms\030\010 \001(\003H\006\210\001\001\022\031\n\014being_edited\030\t " +
      "\001(\010H\007\210\001\001\022>\n\021rating_categories\030\016 \003(\0132#.pl" +
      "_types.ProjectPostRatingCategory\022,\n\007rati" +
      "ngs\030\r \003(\0132\033.pl_types.ProjectPostRatingB\005" +
      "\n\003_idB\t\n\007_user_xB\n\n\010_projectB\007\n\005_nameB\022\n" +
      "\020_long_descr_htmlB\023\n\021_desired_feedbackB\017" +
      "\n\r_post_time_msB\017\n\r_being_editedJ\004\010\n\020\013R\n" +
      "project_idB*\n&org.davincischools.leo.pro" +
      "tos.pl_typesP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_pl_types_Assignment_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_pl_types_Assignment_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_Assignment_descriptor,
        new java.lang.String[] { "Id", "Name", "Nickname", "ShortDescr", "LongDescrHtml", "ClassX", "KnowledgeAndSkills", "ProjectDefinitions", "Id", "Name", "Nickname", "ShortDescr", "LongDescrHtml", "ClassX", });
    internal_static_pl_types_ClassX_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_pl_types_ClassX_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ClassX_descriptor,
        new java.lang.String[] { "Id", "School", "Name", "Number", "Period", "Grade", "ShortDescr", "LongDescrHtml", "Assignments", "KnowledgeAndSkills", "Id", "School", "Name", "Number", "Period", "Grade", "ShortDescr", "LongDescrHtml", });
    internal_static_pl_types_District_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_pl_types_District_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_District_descriptor,
        new java.lang.String[] { "Id", "Name", "Id", "Name", });
    internal_static_pl_types_KnowledgeAndSkill_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_pl_types_KnowledgeAndSkill_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_KnowledgeAndSkill_descriptor,
        new java.lang.String[] { "Id", "Type", "Name", "Category", "ShortDescr", "LongDescrHtml", "Global", "UserX", "Id", "Type", "Name", "Category", "ShortDescr", "LongDescrHtml", "Global", "UserX", });
    internal_static_pl_types_Project_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_pl_types_Project_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_Project_descriptor,
        new java.lang.String[] { "Id", "Name", "ShortDescr", "LongDescrHtml", "Favorite", "ThumbsState", "ThumbsStateReason", "Archived", "Active", "Assignment", "ProjectDefinition", "Milestones", "Id", "Name", "ShortDescr", "LongDescrHtml", "Favorite", "ThumbsState", "ThumbsStateReason", "Archived", "Active", "Assignment", "ProjectDefinition", });
    internal_static_pl_types_Project_Milestone_descriptor =
      internal_static_pl_types_Project_descriptor.getNestedTypes().get(0);
    internal_static_pl_types_Project_Milestone_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_Project_Milestone_descriptor,
        new java.lang.String[] { "Id", "Name", "Steps", "Id", "Name", });
    internal_static_pl_types_Project_Milestone_Step_descriptor =
      internal_static_pl_types_Project_Milestone_descriptor.getNestedTypes().get(0);
    internal_static_pl_types_Project_Milestone_Step_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_Project_Milestone_Step_descriptor,
        new java.lang.String[] { "Id", "Name", "Id", "Name", });
    internal_static_pl_types_School_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_pl_types_School_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_School_descriptor,
        new java.lang.String[] { "Id", "District", "Name", "Nickname", "Address", "Id", "District", "Name", "Nickname", "Address", });
    internal_static_pl_types_UserX_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_pl_types_UserX_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_UserX_descriptor,
        new java.lang.String[] { "Id", "DistrictId", "FirstName", "LastName", "EmailAddress", "IsAdminX", "AdminXId", "IsTeacher", "TeacherId", "IsStudent", "StudentId", "IsDemo", "IsAuthenticated", "Id", "DistrictId", "FirstName", "LastName", "EmailAddress", "IsAdminX", "AdminXId", "IsTeacher", "TeacherId", "IsStudent", "StudentId", "IsDemo", "IsAuthenticated", });
    internal_static_pl_types_ProjectDefinition_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_pl_types_ProjectDefinition_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectDefinition_descriptor,
        new java.lang.String[] { "Id", "Name", "InputId", "Template", "Selected", "State", "ExistingProject", "ExistingProjectUseType", "Inputs", "Assignment", "Id", "Name", "InputId", "Template", "Selected", "State", "ExistingProject", "ExistingProjectUseType", "Assignment", });
    internal_static_pl_types_ProjectInputValue_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_pl_types_ProjectInputValue_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectInputValue_descriptor,
        new java.lang.String[] { "Category", "FreeTexts", "SelectedIds", "Category", });
    internal_static_pl_types_ProjectInputCategory_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_pl_types_ProjectInputCategory_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectInputCategory_descriptor,
        new java.lang.String[] { "Id", "TypeId", "Name", "ShortDescr", "InputDescr", "Hint", "Placeholder", "ValueType", "MaxNumValues", "Options", "Id", "TypeId", "Name", "ShortDescr", "InputDescr", "Hint", "Placeholder", "ValueType", "MaxNumValues", });
    internal_static_pl_types_ProjectInputCategory_Option_descriptor =
      internal_static_pl_types_ProjectInputCategory_descriptor.getNestedTypes().get(0);
    internal_static_pl_types_ProjectInputCategory_Option_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectInputCategory_Option_descriptor,
        new java.lang.String[] { "Id", "Name", "Category", "ShortDescr", "UserX", "Id", "Name", "Category", "ShortDescr", "UserX", });
    internal_static_pl_types_Tag_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_pl_types_Tag_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_Tag_descriptor,
        new java.lang.String[] { "Text", "UserXId", "Text", "UserXId", });
    internal_static_pl_types_ProjectPostComment_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_pl_types_ProjectPostComment_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectPostComment_descriptor,
        new java.lang.String[] { "Id", "UserX", "ProjectPost", "LongDescrHtml", "PostTimeMs", "BeingEdited", "Id", "UserX", "ProjectPost", "LongDescrHtml", "PostTimeMs", "BeingEdited", });
    internal_static_pl_types_ProjectPostRatingCategory_descriptor =
      getDescriptor().getMessageTypes().get(12);
    internal_static_pl_types_ProjectPostRatingCategory_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectPostRatingCategory_descriptor,
        new java.lang.String[] { "ProjectInputFulfillmentId", "Category", "Value", "ValueType", "ProjectInputFulfillmentId", "Category", "Value", "ValueType", });
    internal_static_pl_types_ProjectPostRating_descriptor =
      getDescriptor().getMessageTypes().get(13);
    internal_static_pl_types_ProjectPostRating_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectPostRating_descriptor,
        new java.lang.String[] { "Id", "UserX", "Rating", "RatingType", "ProjectPost", "KnowledgeAndSkill", "ProjectInputFulfillmentId", "Id", "UserX", "Rating", "RatingType", "ProjectPost", "KnowledgeAndSkill", "ProjectInputFulfillmentId", });
    internal_static_pl_types_ProjectPost_descriptor =
      getDescriptor().getMessageTypes().get(14);
    internal_static_pl_types_ProjectPost_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_pl_types_ProjectPost_descriptor,
        new java.lang.String[] { "Id", "UserX", "Project", "Name", "LongDescrHtml", "DesiredFeedback", "Tags", "Comments", "PostTimeMs", "BeingEdited", "RatingCategories", "Ratings", "Id", "UserX", "Project", "Name", "LongDescrHtml", "DesiredFeedback", "PostTimeMs", "BeingEdited", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
