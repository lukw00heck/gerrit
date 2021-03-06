package(
    default_visibility = ["//visibility:public"],
)

load(":rules.bzl", "polygerrit_bundle")
load("//tools/bzl:genrule2.bzl", "genrule2")
load("//tools/bzl:js.bzl", "bower_component_bundle")

polygerrit_bundle(
    name = "polygerrit_ui",
    srcs = glob(
        [
            "**/*.html",
            "**/*.js",
        ],
        exclude = [
            "bower_components/**",
            "index.html",
            "test/**",
            "**/*_test.html",
        ],
    ),
    outs = ["polygerrit_ui.zip"],
    app = "elements/gr-app.html",
)

bower_component_bundle(
    name = "test_components",
    testonly = 1,
    deps = [
        "//lib/js:iron-test-helpers",
        "//lib/js:test-fixture",
        "//lib/js:web-component-tester",
        "//polygerrit-ui:polygerrit_components.bower_components",
    ],
)

filegroup(
    name = "pg_code",
    srcs = glob(
        [
            "**/*.html",
            "**/*.js",
        ],
        exclude = [
            "bower_components/**",
        ],
    ),
)

filegroup(
    name = "bower_components",
    srcs = glob(
        [
            "bower_components/**/*.html",
            "bower_components/**/*.js",
        ],
    ),
)

genrule2(
    name = "pg_code_zip",
    srcs = [":pg_code"],
    outs = ["pg_code.zip"],
    cmd = " && ".join([
        ("tar -hcf- $(locations :pg_code) |" +
         " tar --strip-components=2 -C $$TMP/ -xf-"),
        "cd $$TMP",
        "find . -exec touch -t 198001010000 '{}' ';'",
        "zip -rq $$ROOT/$@ *",
    ]),
)

sh_test(
    name = "wct_test",
    size = "enormous",
    srcs = ["wct_test.sh"],
    data = [
        "test/common-test-setup.html",
        "test/index.html",
        ":pg_code.zip",
        ":test_components.zip",
    ],
    # Should not run sandboxed.
    tags = [
        "local",
        "manual",
    ],
)

sh_test(
    name = "lint_test",
    size = "large",
    srcs = ["lint_test.sh"],
    data = [
        ".eslintrc.json",
        ":pg_code",
    ],
    # Should not run sandboxed.
    tags = [
        "local",
        "manual",
    ],
)

sh_test(
    name = "polylint_test",
    size = "large",
    srcs = ["polylint_test.sh"],
    data = [
        ":pg_code",
        "//polygerrit-ui:polygerrit_components.bower_components.zip",
    ],
    # Should not run sandboxed.
    tags = [
        "local",
        "manual",
    ],
)

DIRECTORIES = [
    "admin",
    "change",
    "change-list",
    "core",
    "diff",
    "edit",
    "plugins",
    "settings",
    "shared",
    "gr-app",
]

[sh_test(
    name = "template_test_" + directory,
    size = "enormous",
    srcs = ["template_test.sh"],
    args = [directory],
    data = [
        ":pg_code",
        ":template_test_srcs",
        "//polygerrit-ui:polygerrit_components.bower_components.zip",
    ],
    tags = [
        # Should not run sandboxed.
        "local",
        "template",
    ],
) for directory in DIRECTORIES]

# Embed bundle
polygerrit_bundle(
    name = "polygerrit_embed_ui",
    srcs = glob(
        [
            "**/*.html",
            "**/*.js",
        ],
        exclude = [
            "bower_components/**",
            "index.html",
            "test/**",
            "**/*_test.html",
        ],
    ),
    outs = ["polygerrit_embed_ui.zip"],
    app = "embed/embed.html",
)

filegroup(
    name = "embed_test_files",
    srcs = glob(
        [
            "embed/**/*_test.html",
        ],
    ),
)

filegroup(
    name = "template_test_srcs",
    srcs = [
        "template_test_srcs/convert_for_template_tests.py",
        "template_test_srcs/template_test.js",
    ],
)

sh_test(
    name = "embed_test",
    size = "small",
    srcs = ["embed_test.sh"],
    data = [
        "embed/test.html",
        "test/common-test-setup.html",
        ":embed_test_files",
        ":polygerrit_embed_ui.zip",
        ":test_components.zip",
    ],
    # Should not run sandboxed.
    tags = [
        "local",
        "manual",
    ],
)
