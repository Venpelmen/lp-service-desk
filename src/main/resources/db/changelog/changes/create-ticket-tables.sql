create TABLE IF NOT EXISTS ticket
(
    id UUID PRIMARY KEY,
    created_at timestamp,
    updated_at timestamp,
    created_by UUID,
    service_date timestamp,
    service_type UUID,
    status  varchar(100) NOT NULL
);

