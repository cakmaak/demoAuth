--
-- PostgreSQL database dump
--

\restrict SFcysngAg8oGWSXAJAfZ1Bky5WCOfcW8pehvd2MHKVuB388NYoOWdsGIwfDRwlp

-- Dumped from database version 17.11 (Debian 17.11-1.pgdg13+2)
-- Dumped by pg_dump version 17.11 (Debian 17.11-1.pgdg13+2)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.role_assignment_scopes DROP CONSTRAINT IF EXISTS fk_scope_region;
ALTER TABLE IF EXISTS ONLY public.role_assignment_scopes DROP CONSTRAINT IF EXISTS fk_scope_organization;
ALTER TABLE IF EXISTS ONLY public.principals DROP CONSTRAINT IF EXISTS uknr31xyhxht7wsmm8ev1wl5d49;
ALTER TABLE IF EXISTS ONLY public.tenants DROP CONSTRAINT IF EXISTS uk_tenants_code;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS uk_role_tenant_code;
ALTER TABLE IF EXISTS ONLY public.role_inheritances DROP CONSTRAINT IF EXISTS uk_role_inheritance;
ALTER TABLE IF EXISTS ONLY public.principals DROP CONSTRAINT IF EXISTS uk_principal_identity;
ALTER TABLE IF EXISTS ONLY public.permissions DROP CONSTRAINT IF EXISTS uk_permission_tenant_resource_action;
ALTER TABLE IF EXISTS ONLY public.masking_policies DROP CONSTRAINT IF EXISTS uk_masking_policy_tenant_code;
ALTER TABLE IF EXISTS ONLY public.identity_providers DROP CONSTRAINT IF EXISTS uk_identity_provider_tenant_issuer;
ALTER TABLE IF EXISTS ONLY public.data_classes DROP CONSTRAINT IF EXISTS uk_data_class_tenant_code;
ALTER TABLE IF EXISTS ONLY public.tenants DROP CONSTRAINT IF EXISTS tenants_pkey;
ALTER TABLE IF EXISTS ONLY public.roles DROP CONSTRAINT IF EXISTS roles_pkey;
ALTER TABLE IF EXISTS ONLY public.role_permission_grants DROP CONSTRAINT IF EXISTS role_permission_grants_pkey;
ALTER TABLE IF EXISTS ONLY public.role_inheritances DROP CONSTRAINT IF EXISTS role_inheritances_pkey;
ALTER TABLE IF EXISTS ONLY public.role_assignments DROP CONSTRAINT IF EXISTS role_assignments_pkey;
ALTER TABLE IF EXISTS ONLY public.role_assignment_scopes DROP CONSTRAINT IF EXISTS role_assignment_scopes_pkey;
ALTER TABLE IF EXISTS ONLY public.principals DROP CONSTRAINT IF EXISTS principals_pkey;
ALTER TABLE IF EXISTS ONLY public.permissions DROP CONSTRAINT IF EXISTS permissions_pkey;
ALTER TABLE IF EXISTS ONLY public.masking_policies DROP CONSTRAINT IF EXISTS masking_policies_pkey;
ALTER TABLE IF EXISTS ONLY public.identity_providers DROP CONSTRAINT IF EXISTS identity_providers_pkey;
ALTER TABLE IF EXISTS ONLY public.data_classes DROP CONSTRAINT IF EXISTS data_classes_pkey;
ALTER TABLE IF EXISTS ONLY public.authorization_constraints DROP CONSTRAINT IF EXISTS authorization_constraints_pkey;
ALTER TABLE IF EXISTS ONLY platform.regions DROP CONSTRAINT IF EXISTS regions_pkey;
ALTER TABLE IF EXISTS ONLY platform.organizations DROP CONSTRAINT IF EXISTS organizations_pkey;
DROP TABLE IF EXISTS public.tenants;
DROP TABLE IF EXISTS public.roles;
DROP TABLE IF EXISTS public.role_permission_grants;
DROP TABLE IF EXISTS public.role_inheritances;
DROP TABLE IF EXISTS public.role_assignments;
DROP TABLE IF EXISTS public.role_assignment_scopes;
DROP TABLE IF EXISTS public.principals;
DROP TABLE IF EXISTS public.permissions;
DROP TABLE IF EXISTS public.masking_policies;
DROP TABLE IF EXISTS public.identity_providers;
DROP TABLE IF EXISTS public.data_classes;
DROP TABLE IF EXISTS public.authorization_constraints;
DROP TABLE IF EXISTS platform.regions;
DROP TABLE IF EXISTS platform.organizations;
DROP SCHEMA IF EXISTS platform;
--
-- Name: platform; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA platform;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: organizations; Type: TABLE; Schema: platform; Owner: -
--

CREATE TABLE platform.organizations (
    id uuid NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(200) NOT NULL
);


--
-- Name: regions; Type: TABLE; Schema: platform; Owner: -
--

CREATE TABLE platform.regions (
    id uuid NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(200) NOT NULL
);


--
-- Name: authorization_constraints; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.authorization_constraints (
    id uuid NOT NULL,
    constraint_key character varying(50) NOT NULL,
    parameters jsonb,
    is_required boolean NOT NULL,
    role_permission_grant_id uuid NOT NULL,
    tenant_id uuid NOT NULL
);


--
-- Name: data_classes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.data_classes (
    id uuid NOT NULL,
    is_active boolean NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(200) NOT NULL,
    parent_data_class_id uuid,
    policy_reference character varying(300),
    sensitivity_rank integer NOT NULL,
    tenant_id uuid NOT NULL
);


--
-- Name: identity_providers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.identity_providers (
    id uuid NOT NULL,
    is_active boolean NOT NULL,
    audience character varying(200),
    claim_mapping jsonb,
    issuer_uri character varying(500) NOT NULL,
    provider_type character varying(50) NOT NULL,
    realm_name character varying(100),
    tenant_id uuid NOT NULL,
    valid_from timestamp(6) with time zone,
    valid_to timestamp(6) with time zone
);


--
-- Name: masking_policies; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.masking_policies (
    id uuid NOT NULL,
    is_active boolean NOT NULL,
    code character varying(100) NOT NULL,
    parameters jsonb,
    policy_reference character varying(300),
    strategy_key character varying(50) NOT NULL,
    tenant_id uuid NOT NULL
);


--
-- Name: permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.permissions (
    id uuid NOT NULL,
    action_key character varying(100) NOT NULL,
    is_active boolean NOT NULL,
    name character varying(200) NOT NULL,
    resource_key character varying(150) NOT NULL,
    scope_required boolean NOT NULL,
    is_system boolean NOT NULL,
    tenant_id uuid NOT NULL
);


--
-- Name: principals; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.principals (
    id uuid NOT NULL,
    external_key character varying(500) NOT NULL,
    principal_type character varying(30) NOT NULL,
    status character varying(30) NOT NULL,
    display_name_snapshot character varying(300),
    identity_provider_id uuid NOT NULL,
    last_seen_at timestamp(6) with time zone,
    party_id uuid,
    synchronized_at timestamp(6) with time zone,
    tenant_id uuid NOT NULL,
    username_snapshot character varying(200)
);


--
-- Name: role_assignment_scopes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_assignment_scopes (
    id uuid NOT NULL,
    decision character varying(20) NOT NULL,
    include_descendants boolean NOT NULL,
    organization_id uuid,
    region_id uuid,
    role_assignment_id uuid NOT NULL,
    scope_type character varying(30) NOT NULL,
    tenant_id uuid NOT NULL,
    valid_from timestamp(6) with time zone,
    valid_to timestamp(6) with time zone,
    CONSTRAINT chk_role_assignment_scope_target CHECK (((((scope_type)::text = 'TENANT'::text) AND (organization_id IS NULL) AND (region_id IS NULL)) OR (((scope_type)::text = 'ORGANIZATION'::text) AND (organization_id IS NOT NULL) AND (region_id IS NULL)) OR (((scope_type)::text = 'REGION'::text) AND (organization_id IS NULL) AND (region_id IS NOT NULL))))
);


--
-- Name: role_assignments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_assignments (
    id uuid NOT NULL,
    assigned_by_principal_id uuid,
    assignment_source character varying(50) NOT NULL,
    principal_id uuid NOT NULL,
    reason character varying(500),
    revoked_at timestamp(6) with time zone,
    role_id uuid NOT NULL,
    tenant_id uuid NOT NULL,
    valid_from timestamp(6) with time zone,
    valid_to timestamp(6) with time zone
);


--
-- Name: role_inheritances; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_inheritances (
    id uuid NOT NULL,
    is_active boolean NOT NULL,
    child_role_id uuid NOT NULL,
    parent_role_id uuid NOT NULL,
    tenant_id uuid NOT NULL
);


--
-- Name: role_permission_grants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_permission_grants (
    id uuid NOT NULL,
    data_class_id uuid NOT NULL,
    decision character varying(20) NOT NULL,
    masking_policy_id uuid,
    permission_id uuid NOT NULL,
    priority integer NOT NULL,
    role_id uuid NOT NULL,
    tenant_id uuid NOT NULL,
    valid_from timestamp(6) with time zone,
    valid_to timestamp(6) with time zone
);


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
    id uuid NOT NULL,
    is_active boolean NOT NULL,
    code character varying(100) NOT NULL,
    description character varying(500),
    name character varying(200) NOT NULL,
    is_system boolean NOT NULL,
    tenant_id uuid NOT NULL
);


--
-- Name: tenants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tenants (
    id uuid NOT NULL,
    is_active boolean NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(200) NOT NULL
);


--
-- Data for Name: organizations; Type: TABLE DATA; Schema: platform; Owner: -
--

COPY platform.organizations (id, code, name) FROM stdin;
e8f3e8d4-41d1-436b-99eb-06ddc850ec47	OHO	├ûzel Halk Otob├╝sleri
\.


--
-- Data for Name: regions; Type: TABLE DATA; Schema: platform; Owner: -
--

COPY platform.regions (id, code, name) FROM stdin;
2c0d884f-fbd2-4c22-9ead-937ccb1f78c3	ANKARA	Ankara
\.


--
-- Data for Name: authorization_constraints; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.authorization_constraints (id, constraint_key, parameters, is_required, role_permission_grant_id, tenant_id) FROM stdin;
d574bf54-fecd-48d0-8fba-546af0c390d5	SELF	{}	t	af45ce2f-9ad9-470b-b316-5eb504423a30	6e973292-9dd3-467c-8b9d-dedc2169699e
\.


--
-- Data for Name: data_classes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.data_classes (id, is_active, code, name, parent_data_class_id, policy_reference, sensitivity_rank, tenant_id) FROM stdin;
997b9fad-189e-4be9-80e2-4547e5a659e6	t	INTERNAL	Kurum ─░├ği	\N	INTERNAL-DATA	2	6e973292-9dd3-467c-8b9d-dedc2169699e
\.


--
-- Data for Name: identity_providers; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.identity_providers (id, is_active, audience, claim_mapping, issuer_uri, provider_type, realm_name, tenant_id, valid_from, valid_to) FROM stdin;
9c5237ff-3567-458e-9d91-8079646f3069	t	demo-client	{}	http://localhost:8081/realms/demo-realm	KEYCLOAK_OIDC	demo-realm	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-08 12:03:25.508936+00	\N
\.


--
-- Data for Name: masking_policies; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.masking_policies (id, is_active, code, parameters, policy_reference, strategy_key, tenant_id) FROM stdin;
\.


--
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.permissions (id, action_key, is_active, name, resource_key, scope_required, is_system, tenant_id) FROM stdin;
3283f02b-ed2e-4326-a4e2-96512a9e5860	VIEW	t	Ara├ğlar─▒ G├Âr├╝nt├╝leme	fleet.vehicle	t	f	6e973292-9dd3-467c-8b9d-dedc2169699e
41bbd68c-be28-4f91-88cc-748c4c70cf61	VIEW	t	GTFS verisini g├Âr├╝nt├╝leyebilir	gtfs.data	t	f	6e973292-9dd3-467c-8b9d-dedc2169699e
cbf409a6-dc31-435e-95cf-53f1f6c17794	UPLOAD	t	GTFS verisi y├╝kleyebilir	gtfs.data	t	f	6e973292-9dd3-467c-8b9d-dedc2169699e
cea07a88-4d47-431b-ad4a-1c683103a241	EXPORT	t	GTFS verisi indirebilir	gtfs.data	t	f	6e973292-9dd3-467c-8b9d-dedc2169699e
\.


--
-- Data for Name: principals; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.principals (id, external_key, principal_type, status, display_name_snapshot, identity_provider_id, last_seen_at, party_id, synchronized_at, tenant_id, username_snapshot) FROM stdin;
8b00a009-93be-4104-807a-2927fe1a6b89	ccb563b9-8704-4b36-bae0-ce1b03643264	USER	ACTIVE	Mehmet lale	9c5237ff-3567-458e-9d91-8079646f3069	2026-09-08 12:24:32.364148+00	\N	2026-09-08 12:24:32.364148+00	6e973292-9dd3-467c-8b9d-dedc2169699e	mehmet
fc8d9f8e-12dc-420f-9f16-d0f0565f18f6	85f491ad-f5dd-48ca-ba33-a862cf746d03	USER	ACTIVE	YUSUF ZIYA CAKMAK	9c5237ff-3567-458e-9d91-8079646f3069	2026-09-12 13:32:29.312061+00	\N	2026-09-12 13:32:29.312061+00	6e973292-9dd3-467c-8b9d-dedc2169699e	yusuf
\.


--
-- Data for Name: role_assignment_scopes; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.role_assignment_scopes (id, decision, include_descendants, organization_id, region_id, role_assignment_id, scope_type, tenant_id, valid_from, valid_to) FROM stdin;
3e80c5ea-a8b3-43fe-b537-139d3af78ceb	INCLUDE	f	e8f3e8d4-41d1-436b-99eb-06ddc850ec47	\N	0d4d4dec-ec1f-4507-9685-baaf2b440d32	ORGANIZATION	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-08 14:32:48.553012+00	\N
32a89292-d138-4b98-ae71-b66a60e0d4e9	INCLUDE	f	\N	2c0d884f-fbd2-4c22-9ead-937ccb1f78c3	0d4d4dec-ec1f-4507-9685-baaf2b440d32	REGION	6e973292-9dd3-467c-8b9d-dedc2169699e	2099-01-01 00:00:00+00	\N
b10e0eb8-c327-473e-bd19-a2111fd267e3	INCLUDE	f	\N	2c0d884f-fbd2-4c22-9ead-937ccb1f78c3	0d4d4dec-ec1f-4507-9685-baaf2b440d32	REGION	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-08 14:32:48.553012+00	\N
4850a46b-6ed5-4923-a577-c960aa878314	INCLUDE	f	\N	2c0d884f-fbd2-4c22-9ead-937ccb1f78c3	46fc8cb1-b102-4b11-8322-e9b74f521f95	REGION	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-10 10:45:20.741666+00	\N
f68a7036-fa86-4560-a57b-64739aa4c259	INCLUDE	f	\N	2c0d884f-fbd2-4c22-9ead-937ccb1f78c3	e186d8ea-53be-45c6-aa9c-6fc5e14e7775	REGION	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 13:22:40.850614+00	\N
\.


--
-- Data for Name: role_assignments; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.role_assignments (id, assigned_by_principal_id, assignment_source, principal_id, reason, revoked_at, role_id, tenant_id, valid_from, valid_to) FROM stdin;
0d4d4dec-ec1f-4507-9685-baaf2b440d32	\N	MANUAL	fc8d9f8e-12dc-420f-9f16-d0f0565f18f6	Fleet y├Ânetim yetkisi	2026-09-10 10:40:57.580977+00	36c3be94-c468-4c54-93d3-4e0ece46789b	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-08 13:16:39.407512+00	\N
46fc8cb1-b102-4b11-8322-e9b74f521f95	\N	MANUAL	fc8d9f8e-12dc-420f-9f16-d0f0565f18f6	CEO inheritance testi	\N	83eea208-e3a6-4d6a-b37c-43d354239f7b	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-10 10:41:55.379557+00	\N
e186d8ea-53be-45c6-aa9c-6fc5e14e7775	\N	MANUAL	fc8d9f8e-12dc-420f-9f16-d0f0565f18f6	Fleet y├Ânetim yetkisi	\N	9b4ce8ad-ccd5-4ac3-b74a-a60fecd04c4e	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 12:30:11.230701+00	\N
\.


--
-- Data for Name: role_inheritances; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.role_inheritances (id, is_active, child_role_id, parent_role_id, tenant_id) FROM stdin;
953396b4-f8e6-44d9-b2b4-6c6355b96e47	t	36c3be94-c468-4c54-93d3-4e0ece46789b	83eea208-e3a6-4d6a-b37c-43d354239f7b	6e973292-9dd3-467c-8b9d-dedc2169699e
\.


--
-- Data for Name: role_permission_grants; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.role_permission_grants (id, data_class_id, decision, masking_policy_id, permission_id, priority, role_id, tenant_id, valid_from, valid_to) FROM stdin;
f7fc56ce-df4a-48dd-816f-aaf25efea49c	997b9fad-189e-4be9-80e2-4547e5a659e6	DENY	\N	3283f02b-ed2e-4326-a4e2-96512a9e5860	200	36c3be94-c468-4c54-93d3-4e0ece46789b	6e973292-9dd3-467c-8b9d-dedc2169699e	2099-01-01 00:00:00+00	\N
c6a8c650-ad96-4e9c-bd75-83b96c75d072	997b9fad-189e-4be9-80e2-4547e5a659e6	ALLOW	\N	cea07a88-4d47-431b-ad4a-1c683103a241	100	2af64b77-72c6-4092-b5a6-1c1b54508883	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-10 14:09:15.626951+00	\N
e5351dbd-c3e4-4d4f-9ecc-1385e4fc3450	997b9fad-189e-4be9-80e2-4547e5a659e6	ALLOW	\N	3283f02b-ed2e-4326-a4e2-96512a9e5860	100	2af64b77-72c6-4092-b5a6-1c1b54508883	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 11:19:00.860662+00	\N
7f8f04eb-923a-437c-9219-426201e07ca7	997b9fad-189e-4be9-80e2-4547e5a659e6	ALLOW	\N	cbf409a6-dc31-435e-95cf-53f1f6c17794	100	b5746753-5509-4e92-92cb-ba5d55ce1c18	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 11:57:37.939808+00	\N
ab7ad30b-2c97-4c04-aaa7-72bfdbfe0d7d	997b9fad-189e-4be9-80e2-4547e5a659e6	ALLOW	\N	3283f02b-ed2e-4326-a4e2-96512a9e5860	100	b5746753-5509-4e92-92cb-ba5d55ce1c18	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 12:00:32.00687+00	\N
689126fc-86cc-4fb8-b275-5022e462263e	997b9fad-189e-4be9-80e2-4547e5a659e6	ALLOW	\N	41bbd68c-be28-4f91-88cc-748c4c70cf61	100	2af64b77-72c6-4092-b5a6-1c1b54508883	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 12:00:53.406865+00	\N
b8fa191f-18ff-4048-9e2b-98064ba2b869	997b9fad-189e-4be9-80e2-4547e5a659e6	ALLOW	\N	41bbd68c-be28-4f91-88cc-748c4c70cf61	100	b5746753-5509-4e92-92cb-ba5d55ce1c18	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 12:01:03.830096+00	\N
940a920d-addd-45bb-a5da-56938fbef27d	997b9fad-189e-4be9-80e2-4547e5a659e6	ALLOW	\N	cea07a88-4d47-431b-ad4a-1c683103a241	100	b5746753-5509-4e92-92cb-ba5d55ce1c18	6e973292-9dd3-467c-8b9d-dedc2169699e	2026-09-11 12:01:19.114491+00	\N
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.roles (id, is_active, code, description, name, is_system, tenant_id) FROM stdin;
36c3be94-c468-4c54-93d3-4e0ece46789b	t	FLEET_MANAGER	Filo y├Ânetim yetkilerine sahip rol	Fleet Manager	f	6e973292-9dd3-467c-8b9d-dedc2169699e
83eea208-e3a6-4d6a-b37c-43d354239f7b	t	FLEET_ceo	Filo y├Ânetim yetkilerine sahip rol	Fleet CODE	f	6e973292-9dd3-467c-8b9d-dedc2169699e
9b4ce8ad-ccd5-4ac3-b74a-a60fecd04c4e	t	OBSERVER	Sistemdeki verileri g├Âr├╝nt├╝leyebilen kullan─▒c─▒	G├Âzlemci	f	6e973292-9dd3-467c-8b9d-dedc2169699e
b1e54c07-fe5c-425c-89b9-8c51e0421e99	t	ANALYST	Analiz i┼şlemlerini ger├ğekle┼ştirebilen kullan─▒c─▒	Analist	f	6e973292-9dd3-467c-8b9d-dedc2169699e
2af64b77-72c6-4092-b5a6-1c1b54508883	t	ADMIN	Sistem y├Âneticisi	Admin	t	6e973292-9dd3-467c-8b9d-dedc2169699e
817ceea5-68b0-4978-866a-8a5ef09302f4	t	PARABOL_USER	Parabol kullan─▒c─▒ rol├╝	Parabol User	f	6e973292-9dd3-467c-8b9d-dedc2169699e
b5746753-5509-4e92-92cb-ba5d55ce1c18	t	ARIZABAKIMCI	ara├ğlar─▒n ar─▒za ve bak─▒m─▒na m├╝dahale edebilir	ar─▒za bak─▒m muduru	f	6e973292-9dd3-467c-8b9d-dedc2169699e
\.


--
-- Data for Name: tenants; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tenants (id, is_active, code, name) FROM stdin;
6e973292-9dd3-467c-8b9d-dedc2169699e	t	EGO	EGO Genel M├╝d├╝rl├╝─ş├╝
\.


--
-- Name: organizations organizations_pkey; Type: CONSTRAINT; Schema: platform; Owner: -
--

ALTER TABLE ONLY platform.organizations
    ADD CONSTRAINT organizations_pkey PRIMARY KEY (id);


--
-- Name: regions regions_pkey; Type: CONSTRAINT; Schema: platform; Owner: -
--

ALTER TABLE ONLY platform.regions
    ADD CONSTRAINT regions_pkey PRIMARY KEY (id);


--
-- Name: authorization_constraints authorization_constraints_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.authorization_constraints
    ADD CONSTRAINT authorization_constraints_pkey PRIMARY KEY (id);


--
-- Name: data_classes data_classes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.data_classes
    ADD CONSTRAINT data_classes_pkey PRIMARY KEY (id);


--
-- Name: identity_providers identity_providers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.identity_providers
    ADD CONSTRAINT identity_providers_pkey PRIMARY KEY (id);


--
-- Name: masking_policies masking_policies_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.masking_policies
    ADD CONSTRAINT masking_policies_pkey PRIMARY KEY (id);


--
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- Name: principals principals_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.principals
    ADD CONSTRAINT principals_pkey PRIMARY KEY (id);


--
-- Name: role_assignment_scopes role_assignment_scopes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_assignment_scopes
    ADD CONSTRAINT role_assignment_scopes_pkey PRIMARY KEY (id);


--
-- Name: role_assignments role_assignments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_assignments
    ADD CONSTRAINT role_assignments_pkey PRIMARY KEY (id);


--
-- Name: role_inheritances role_inheritances_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_inheritances
    ADD CONSTRAINT role_inheritances_pkey PRIMARY KEY (id);


--
-- Name: role_permission_grants role_permission_grants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permission_grants
    ADD CONSTRAINT role_permission_grants_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: tenants tenants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tenants
    ADD CONSTRAINT tenants_pkey PRIMARY KEY (id);


--
-- Name: data_classes uk_data_class_tenant_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.data_classes
    ADD CONSTRAINT uk_data_class_tenant_code UNIQUE (tenant_id, code);


--
-- Name: identity_providers uk_identity_provider_tenant_issuer; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.identity_providers
    ADD CONSTRAINT uk_identity_provider_tenant_issuer UNIQUE (tenant_id, issuer_uri);


--
-- Name: masking_policies uk_masking_policy_tenant_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.masking_policies
    ADD CONSTRAINT uk_masking_policy_tenant_code UNIQUE (tenant_id, code);


--
-- Name: permissions uk_permission_tenant_resource_action; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT uk_permission_tenant_resource_action UNIQUE (tenant_id, resource_key, action_key);


--
-- Name: principals uk_principal_identity; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.principals
    ADD CONSTRAINT uk_principal_identity UNIQUE (tenant_id, identity_provider_id, principal_type, external_key);


--
-- Name: role_inheritances uk_role_inheritance; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_inheritances
    ADD CONSTRAINT uk_role_inheritance UNIQUE (tenant_id, parent_role_id, child_role_id);


--
-- Name: roles uk_role_tenant_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT uk_role_tenant_code UNIQUE (tenant_id, code);


--
-- Name: tenants uk_tenants_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tenants
    ADD CONSTRAINT uk_tenants_code UNIQUE (code);


--
-- Name: principals uknr31xyhxht7wsmm8ev1wl5d49; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.principals
    ADD CONSTRAINT uknr31xyhxht7wsmm8ev1wl5d49 UNIQUE (external_key);


--
-- Name: role_assignment_scopes fk_scope_organization; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_assignment_scopes
    ADD CONSTRAINT fk_scope_organization FOREIGN KEY (organization_id) REFERENCES platform.organizations(id);


--
-- Name: role_assignment_scopes fk_scope_region; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_assignment_scopes
    ADD CONSTRAINT fk_scope_region FOREIGN KEY (region_id) REFERENCES platform.regions(id);


--
-- PostgreSQL database dump complete
--

\unrestrict SFcysngAg8oGWSXAJAfZ1Bky5WCOfcW8pehvd2MHKVuB388NYoOWdsGIwfDRwlp

